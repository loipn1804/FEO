package com.fareastorchid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.ReserveAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.KeyboardVisibilityListener.OnKeyboardVisibilityListener;
import com.fareastorchid.listener.KeyboardVisibilityListener;
import com.fareastorchid.listener.OnOrderListener;
import com.fareastorchid.listener.OnSpecialRequestListener;
import com.fareastorchid.model.DetailItem;
import com.fareastorchid.model.Discount;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;
import com.fareastorchid.view.MessageDialog;
import com.fareastorchid.view.OrderDialog;
import com.fareastorchid.view.OrderDialog.OrderButtonPressListener;
import com.fareastorchid.view.UpdateSpecialRequestDialog;

public class ReserveActivity extends AbstractActivity implements
		OnOrderListener, OrderButtonPressListener, OnSpecialRequestListener,
		OnKeyboardVisibilityListener {

	private ImageView imvConfirmOrder;
	private ListView lvReserve;
	private ReserveAdapter listAdapter;
	private TextView tvTotal;
	private EditText edt_remark;
	private double subTotal;
	private SharedPreferences sharedpreferences;
	private OrderDialog guestDialog;
	private RelativeLayout rlCheck;
	private CheckBox chkTerms;
	private UpdateSpecialRequestDialog upRequestDialog;
	private TextView tvAddress;
	private TextView tvTime;
	private LinearLayout layoutAddTime, reserveTotalLayout,
			lnlReserveTotalLayout;
	private FloristBusiness doPushOrderItems = new FloristBusinessImpl();
	private KeyboardVisibilityListener keyboardVisibilityListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_reserve);
		keyboardVisibilityListener = new KeyboardVisibilityListener();
		keyboardVisibilityListener.setKeyboardListener(this,
				ReserveActivity.this, R.id.rootview);
		Controller.updateActInstance(this);
		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES,
				Context.MODE_PRIVATE);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!GlobalData.chosenAddress.equalsIgnoreCase("")) {
			tvAddress.setText(GlobalData.chosenAddress);
		}
		if (!GlobalData.chosenTimeSlot.equalsIgnoreCase("")) {
			tvTime.setText(GlobalData.chosenTimeSlot);
		}

		SharedPreferences preferences = getSharedPreferences("remark",
				MODE_PRIVATE);
		// Editor editor = preferences.edit();
		String remark = preferences.getString("text", "");
		edt_remark.setText(remark);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences preferences = getSharedPreferences("remark",
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("text", edt_remark.getText().toString());
		editor.commit();
	}

	private void initView() {
		initCommonView();
		setHeaderTitle("Order Summary");

		lvReserve = (ListView) findViewById(R.id.lvReserve);
		edt_remark = (EditText) findViewById(R.id.edt_remark);
		chkTerms = (CheckBox) findViewById(R.id.cbTotal);
		rlCheck = (RelativeLayout) findViewById(R.id.reserve_rl_check);
		tvTotal = (TextView) findViewById(R.id.reserve_total);
		imvConfirmOrder = (ImageView) findViewById(R.id.reserve_confirm_order);
		imvConfirmOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!chkTerms.isChecked()) {
					SimpleToast.error(ReserveActivity.this,
							"You have to accept the terms and conditions");
				} else if (GlobalData.isLogin
						&& GlobalData.chosenAddress.equalsIgnoreCase("")) {
					SimpleToast.error(ReserveActivity.this,
							"You have to choose delivery address");
				} else if (GlobalData.isLogin
						&& GlobalData.chosenTimeSlot.equalsIgnoreCase("")) {
					SimpleToast.error(ReserveActivity.this,
							"You have to choose delivery timeslot");
				} else if (PQT.isListDirty(GlobalData.listOrderItem)) {
					if (GlobalData.isLogin) {
						JSONArray itemListArray = new JSONArray();
						String userId = "";
						for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
							OrderItem item = GlobalData.listOrderItem.get(i);
							// Log.w("OrderItem", item.toString());

							itemListArray.put(String2Json(item));
						}
						if (sharedpreferences.contains(GlobalData.Id)) {
							userId = sharedpreferences.getString(GlobalData.Id,
									"");
						}
						String remark = "";
						if (!edt_remark.getText().toString()
								.equalsIgnoreCase(""))
							remark = edt_remark.getText().toString();
						pushOrderItem(itemListArray, userId, "", "", "", "",
								remark, GlobalData.chosenAddress,
								GlobalData.chosenTimeSlot);
					} else {
						guestDialog = new OrderDialog(ReserveActivity.this,
								ReserveActivity.this);
						guestDialog.show();
					}
					GlobalData.chosenAddress = "";
					GlobalData.chosenTimeSlot = "";
				}
			}
		});
		lvReserve = (ListView) findViewById(R.id.lvReserve);
		tvAddress = (TextView) findViewById(R.id.reserve_address);
		tvTime = (TextView) findViewById(R.id.reserve_timeslot);
		layoutAddTime = (LinearLayout) findViewById(R.id.reserve_add_time);
		if (GlobalData.isLogin) {
			layoutAddTime.setVisibility(View.VISIBLE);
			tvAddress.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(ReserveActivity.this,
							DeliveryAddressActivity.class);
					startActivity(i);
				}
			});

			tvTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(ReserveActivity.this,
							DeliveryTimeActivity.class);
					startActivity(i);
				}
			});
		} else {
			layoutAddTime.setVisibility(View.GONE);
		}

		rlCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chkTerms.setChecked(!chkTerms.isChecked());
			}
		});
		reserveTotalLayout = (LinearLayout) findViewById(R.id.reserveTotalLayout);
		lnlReserveTotalLayout = (LinearLayout) findViewById(R.id.lnlReserveTotalLayout);
	}

	private void initData() {
		listAdapter = new ReserveAdapter(this);
		listAdapter.setListItem(GlobalData.listOrderItem);
		lvReserve.setAdapter(listAdapter);
		setTotal();
	}

	public void setTotal() {
		subTotal = 0;
		for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
			subTotal += (GlobalData.listOrderItem.get(i).getSellingPrice() * GlobalData.listOrderItem
					.get(i).getQuantity());
		}

		// GST = subTotal * 7 / 100;
		// total = subTotal + GST;

		tvTotal.setText("S$ " + Functions.round2String(subTotal));
	}

	public void clearTotal() {
		tvTotal.setText("S$ 0");
	}

	public JSONObject String2Json(OrderItem item) {
		JSONObject json = new JSONObject();
		try {
			json.put("id_item", item.getItem().getId());
			json.put("amount", item.getQuantity());
			json.put("base_price", item.getBasePrice());
			json.put("selling_price", item.getSellingPrice());
			json.put("special_request", item.getSpecReq());
			json.put("delivery_address", item.getAddress());
			json.put("delivery_time", item.getTimeSlot());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	private void pushOrderItem(JSONArray array, String userId,
			String guestName, String guestPhone, String guestPhone2,
			String guestAddress, String remark, String delieveryAddress,
			String deliveryTimeslot) {
		doPushOrderItems.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(
								""))
							SimpleToast.error(Controller
									.getActInstance(MainActivity.class),
									error_message.getError_message());
						else {
							SimpleToast.error(Controller
									.getActInstance(MainActivity.class),
									R.string.err_connect);
						}
					}
				});
			}

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

			@Override
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							String message = root.getString("message");
							// SimpleToast.info(ReserveActivity.this, message);
							clearOrder();
							// Functions.showDialogMessageAndFinish(message,
							// ReserveActivity.this);
							new MessageDialog(ReserveActivity.this, "",
									message, false).show();
						} catch (JSONException e) {
							e.printStackTrace();
						} finally {
							hideProgressBar();
						}
					}
				});
			}
		});
		doPushOrderItems.pushOrderItems(array, userId, guestName, guestPhone,
				guestPhone2, guestAddress, remark, delieveryAddress,
				deliveryTimeslot);
	}

	private void clearOrder() {
		if (GlobalData.listOrderItem != null) {
			GlobalData.chosenTimeSlot = "";
			GlobalData.listOrderItem.clear();
			listAdapter.notifyDataSetChanged();
		}

		edt_remark.setText("");
		tvTotal.setText("S$ 0.00");
		chkTerms.setChecked(false);
		tvAddress.setText("Delivery Address");
		tvTime.setText("Delivery Time");
	}

	@Override
	public void onDeteleOrder() {
		subTotal = 0;
		setTotal();
	}

	@Override
	public void onConfirmButtonPress(String name, String phone1, String phone2,
			String email) {
		guestDialog.dismiss();
		JSONArray itemListArray = new JSONArray();
		for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
			itemListArray.put(String2Json(GlobalData.listOrderItem.get(i)));
		}
		String remark = "";
		if (!edt_remark.getText().toString().equalsIgnoreCase(""))
			remark = edt_remark.getText().toString();
		pushOrderItem(itemListArray, "", name, phone1, phone2, email, remark,
				"", "");
	}

	@Override
	public void onUpdateSpecialRequest(int orderItemPos) {
		upRequestDialog = new UpdateSpecialRequestDialog(this, orderItemPos);
		upRequestDialog.show();
	}

	@Override
	public void onPostUpdateSpecialRequest(int orderItemPos,
			String specialRequest, int quantity) {
		Log.e("ThuanPQ", String.format("specialRequest: %s - quantity: %d",
				specialRequest, quantity));

		if (orderItemPos >= 0 && PQT.isListDirty(GlobalData.listOrderItem)) {
			GlobalData.listOrderItem.get(orderItemPos).setSpecReq(
					specialRequest);
			GlobalData.listOrderItem.get(orderItemPos).setQuantity(quantity);

			// listAdapter.notifyDataSetChanged();

			// update selling price
			DetailItem currentItem = GlobalData.listOrderItem.get(orderItemPos)
					.getItem();
			double price = 0;

			if (GlobalData.isLogin) {
				price = Double.valueOf(currentItem.getTrader_price());
			} else {
				price = Double.valueOf(currentItem.getWholesale_price());
			}

			double basePrice = price;
			if (currentItem.getLsDiscount() != null && GlobalData.isLogin) {
				int latestQty = 0;
				for (int i = 0; i < currentItem.getLsDiscount().size(); i++) {
					int curQty = Integer.valueOf(currentItem.getLsDiscount()
							.get(i).getTotal_item());
					if (quantity >= curQty && curQty > latestQty) {
						latestQty = curQty;

						price = basePrice
								- Double.valueOf(currentItem.getLsDiscount()
										.get(i).getAmount());
					}
				}
			}

			GlobalData.listOrderItem.get(orderItemPos).setSellingPrice(
					Double.valueOf(price));

			OrderItem item = GlobalData.listOrderItem.get(orderItemPos);

			int cur_quantity = 0;
			if (GlobalData.isLogin && item.getItem().getLsDiscount() != null
					&& item.getItem().getLsDiscount().size() > 0) {
				for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
					OrderItem listItem = GlobalData.listOrderItem.get(i);
					if (listItem.getId().equalsIgnoreCase(item.getId())) {
						cur_quantity += listItem.getQuantity();
					}
				}
				boolean isHaveDis = false;
				for (int j = item.getItem().getLsDiscount().size() - 1; j >= 0; j--) {
					Discount dis = item.getItem().getLsDiscount().get(j);
					if (cur_quantity >= Integer.valueOf(dis.getTotal_item())) {
						GlobalData.updateDiscountPrice(currentItem
								.getCode_name());
						isHaveDis = true;
						break;
					}
				}
				if (!isHaveDis) {
					GlobalData.updateDiscountPrice(currentItem.getCode_name());
				}
			}

			listAdapter.notifyDataSetChanged();

			setTotal();
		}

		upRequestDialog.dismiss();
	}

	@Override
	public void onViewOrder() {

	}

	private class MessageDialog extends Dialog {

		public MessageDialog(final Context context, final String title,
				final String message, final boolean isFinish) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			setContentView(R.layout.view_message_popup);

			// ((TextView) findViewById(R.id.popup_tv_title)).setText(title);

			((TextView) findViewById(R.id.popup_tv_message)).setText(message);
			((TextView) findViewById(R.id.popup_tv_message))
					.setMovementMethod(new ScrollingMovementMethod());
			ImageButton imbYes = (ImageButton) findViewById(R.id.popup_imb_yes);

			imbYes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SharedPreferences preferences = getSharedPreferences(
							"remark", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("text", "");
					editor.commit();

					dismiss();

					Intent backToHome = new Intent();
					backToHome.setClass(ReserveActivity.this,
							MainActivity.class);
					backToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(backToHome);
				}
			});
		}
	}

	@Override
	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub
		if (visible) {
			lnlReserveTotalLayout.setVisibility(View.GONE);
		} else {
			lnlReserveTotalLayout.setVisibility(View.VISIBLE);
		}
	}
}
