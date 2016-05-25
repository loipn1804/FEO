package com.fareastorchid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.fareastorchid.adapter.DeliveryAddressAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnAddressListener;
import com.fareastorchid.model.DelAddress;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.view.AddAddressDialog;
import com.fareastorchid.view.AddAddressDialog.AddAddressDialogListener;
import com.fareastorchid.view.EditAddressDialog;
import com.fareastorchid.view.EditAddressDialog.EditButtonPressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pierry.simpletoast.SimpleToast;

public class DeliveryAddressActivity extends AbstractActivity implements
		OnAddressListener, EditButtonPressListener, AddAddressDialogListener,
		DeliveryAddressAdapter.UpdateOutletAddressCbk {

	private SharedPreferences sharedpreferences;
	private ImageView ivConfirm;
	// private CheckBox chkTerm;
	private ListView lvAddress;
	private DeliveryAddressAdapter adapter;
	private List<DelAddress> listAddress = new ArrayList<DelAddress>();
	private EditAddressDialog dialog;
	private AddAddressDialog addDialog;
	private String userId;
	private boolean isFormMyAccount = false;
	private FloristBusiness doGetDeliveryAddress = new FloristBusinessImpl();
	private FloristBusiness doEditAddress = new FloristBusinessImpl();
	private FloristBusiness doAddAddress = new FloristBusinessImpl();
	private FloristBusiness doDeleteAddress = new FloristBusinessImpl();
	private FloristBusiness doUpdateOutletAddress = new FloristBusinessImpl();
	private Dialog dialogUpdateAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_address);
		
		Controller.updateActInstance(this);

		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES,
				Context.MODE_PRIVATE);
		Intent i = getIntent();
		isFormMyAccount = i.getBooleanExtra("fromMyAccount", false);
		initView();
		initData();
	}

	private void initView() {
		initCommonView();
		if (isFormMyAccount) {
			setHeaderTitle("My Outlets");
			findViewById(R.id.tvDelivery).setVisibility(View.GONE);
		} else {
			setHeaderTitle("Delivery Address");
		}

		// chkTerm = (CheckBox) findViewById(R.id.cbTerm);
		ivConfirm = (ImageView) findViewById(R.id.delivery_confirm);
		ivConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
					// if (chkTerm.isChecked()) {
					GlobalData.listOrderItem.get(i).setAddress(
							GlobalData.chosenAddress);
					// } else if (i == GlobalData.editPos) {
					// GlobalData.listOrderItem.get(i).setAddress(GlobalData.chosenAddress);
					// break;
					// }
				}
				finish();
			}
		});
		if (isFormMyAccount) {
			ivConfirm.setVisibility(View.GONE);
		} else {
			ivConfirm.setVisibility(View.VISIBLE);
		}
		lvAddress = (ListView) findViewById(R.id.lvCurrentAddress);
		adapter = new DeliveryAddressAdapter(this, isFormMyAccount, this);

		if (GlobalData.isLogin) {
			View footerView = getLayoutInflater().inflate(
					R.layout.view_add_address_footer, lvAddress, false);
			ImageView ivAdd = (ImageView) footerView
					.findViewById(R.id.delivery_add);
			ivAdd.setVisibility(View.GONE);
			footerView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					addAddress();
				}
			});
			if (!isFormMyAccount) {
				lvAddress.addFooterView(footerView);
			}
		} else {
		}
	}

	private void initData() {
		lvAddress.setAdapter(adapter);
		userId = "";
		if (GlobalData.isLogin) {
			Log.w("isLogin", "isLogin");
			if (sharedpreferences.contains(GlobalData.Id)) {
				userId = sharedpreferences.getString(GlobalData.Id, "");
			}
			// getDeliveryAddress(userId);
			initDeliveryAddress(userId);
		} else {
			// getDeliveryAddress(userId);
			initDeliveryAddress(userId);
		}
	}

	private void initDeliveryAddress(String userId) {
		try {
			JSONObject self = new JSONObject();
			self.put("id", "0");
			self.put("address", "Self - Collection");
			self.put("id_user",
					sharedpreferences.getString(GlobalData.ShopName, ""));
			DelAddress address_self = new DelAddress(self);
			if (!isFormMyAccount) {
				listAddress.add(address_self);
			}

			JSONArray jarrOutlet = new JSONArray(sharedpreferences.getString(
					GlobalData.Outlet, "[]"));
			for (int i = 0; i < jarrOutlet.length(); i++) {
				JSONObject outlet = jarrOutlet.getJSONObject(i);
				JSONObject data = new JSONObject();
				data.put("id", outlet.getString("id"));
				data.put("address", outlet.getString("outlet_address"));
				data.put("id_user", outlet.getString("outlet_name"));
				data.put("contact", outlet.getString("outlet_contact"));
				DelAddress address = new DelAddress(data);
				listAddress.add(address);
			}
			adapter.setListDelAddresss(listAddress);
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateDelieveryAddress(String addressId, String new_address) {
		try {
			JSONArray jarrOutlet = new JSONArray(sharedpreferences.getString(
					GlobalData.Outlet, "[]"));
			for (int i = 0; i < jarrOutlet.length(); i++) {
				JSONObject outlet = jarrOutlet.getJSONObject(i);
				if (outlet.getString("id").equals(addressId)) {
					outlet.put("outlet_address", new_address);
					break;
				}
			}
			Editor editor = sharedpreferences.edit();
			editor.putString(GlobalData.Outlet, jarrOutlet.toString());
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getDeliveryAddress(String userId) {
		doGetDeliveryAddress.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							JSONArray catTypeJSONArray = root
									.getJSONArray("data");
							if (catTypeJSONArray.length() != 0) {
								try {
									// don't add cateId 0
									for (int i = 0; i < catTypeJSONArray
											.length(); i++) {
										JSONObject data = catTypeJSONArray
												.getJSONObject(i);
										DelAddress address = new DelAddress(
												data);
										listAddress.add(address);
									}
									adapter.setListDelAddresss(listAddress);
									adapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							hideProgressBar();
						}
					}
				});
			}
		});
		// doGetDeliveryAddress.getDeliveryAddress(userId);
	}

	private void doEditAddress(String address) {
		doEditAddress.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						listAddress.clear();
						adapter.setListDelAddresss(listAddress);
						adapter.notifyDataSetChanged();
						getDeliveryAddress(userId);
					}
				});
			}
		});
		doEditAddress.updateDeliveryAddress(GlobalData.edtAddress.getId(),
				GlobalData.edtAddress.getUserId(), address);
	}

	private void doAddAddress(String address) {
		doAddAddress.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						listAddress.clear();
						adapter.setListDelAddresss(listAddress);
						adapter.notifyDataSetChanged();
						getDeliveryAddress(userId);
					}
				});
			}
		});
		doAddAddress.addDeliveryAddress(userId, address);
	}

	private void doDeleteAddress(String addressId) {
		doDeleteAddress.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SimpleToast.info(
								Controller.getActInstance(MainActivity.class),
								"Deleted Delivery Address");
						hideProgressBar();
						listAddress.clear();
						adapter.setListDelAddresss(listAddress);
						adapter.notifyDataSetChanged();
						getDeliveryAddress(userId);
					}
				});
			}
		});
		doDeleteAddress.deleteDeliveryAddress(addressId, userId);
	}

	private void doUpdateOutletAddress(final String addressId,
			final String address, String name, String contact) {
		doDeleteAddress.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SimpleToast.info(
								Controller.getActInstance(MainActivity.class),
								"Updated Outlet Address");
						hideProgressBar();
						listAddress.clear();
						// adapter.setListDelAddresss(listAddress);
						// adapter.notifyDataSetChanged();
						// getDeliveryAddress(userId);
						updateDelieveryAddress(addressId, address);
						initDeliveryAddress(userId);
						dialogUpdateAddress.dismiss();
					}
				});
			}
		});
		doDeleteAddress.updateOutletAddress(addressId, address, name, contact);
	}

	private void UpdateAddressOutlet(final String id, final String address,
			final String name, final String contact) {

		dialogUpdateAddress = new Dialog(DeliveryAddressActivity.this);

		dialogUpdateAddress.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogUpdateAddress.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		dialogUpdateAddress.setContentView(R.layout.view_update_address_outlet);

		final EditText edtAddress = (EditText) dialogUpdateAddress
				.findViewById(R.id.popup_edt_specailrequest);
		edtAddress.requestFocus();
		ImageButton imbSend = (ImageButton) dialogUpdateAddress
				.findViewById(R.id.popup_imb_send);

		edtAddress.setText(address);

		imbSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String new_address = edtAddress.getText().toString().trim();
				if (new_address.length() == 0) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Please enter new address");
				} else if (address.equalsIgnoreCase(new_address)) {
					showProgressBar();
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							hideProgressBar();
							SimpleToast.info(Controller
									.getActInstance(MainActivity.class),
									"Updated Outlet Address");
							dialogUpdateAddress.dismiss();
						}
					}, 1000);
				} else {
					doUpdateOutletAddress(id, new_address, name, contact);
				}
			}
		});

		dialogUpdateAddress.show();
	}

	@Override
	public void editAddress() {
		dialog = new EditAddressDialog(this, DeliveryAddressActivity.this);
		dialog.show();
	}

	@Override
	public void onConfirmButtonPress(String address) {
		doEditAddress(address);
		dialog.dismiss();
	}

	public void addAddress() {
		addDialog = new AddAddressDialog(this, DeliveryAddressActivity.this);
		addDialog.show();
	}

	@Override
	public void onConfirmAddButtonPress(String address) {
		// doAddAddress(address);
		// SimpleToast.info(getApplicationContext(), address);
		GlobalData.chosenAddress = address;
		for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
			// if (chkTerm.isChecked()) {
			GlobalData.listOrderItem.get(i)
					.setAddress(GlobalData.chosenAddress);
			// } else if (i == GlobalData.editPos) {
			// GlobalData.listOrderItem.get(i).setAddress(GlobalData.chosenAddress);
			// break;
			// }
		}
		finish();
		addDialog.dismiss();
	}

	@Override
	public void deleteAddress(String address_id) {
		doDeleteAddress(address_id);

	}

	@Override
	public void updateOutletAddress(String id, String address, String name,
			String contact) {
		UpdateAddressOutlet(id, address, name, contact);
	}

}
