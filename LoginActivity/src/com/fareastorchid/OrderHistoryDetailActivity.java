package com.fareastorchid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.OrderDetailAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.DetailItem;
import com.fareastorchid.model.OrderHistoryDetail;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;

public class OrderHistoryDetailActivity extends AbstractActivity {

	// private ImageView ivBack;
	private ListView lvOrderHistoryDetail;
	private OrderDetailAdapter listAdapter;
	private List<OrderHistoryDetail> listItems = new ArrayList<OrderHistoryDetail>();
	private TextView tvTotal;
	private String id_transaction, title, delivery_address, delivery_time,
			remark;
	private ImageView imvAddAlltoCart;
	private TextView detailAddress, detailTime, txtUpdate;
	private EditText edt_remark;
	private FloristBusiness doGetOrderHistoryDetail = new FloristBusinessImpl();
	private FloristBusiness doUpdateOrderHistoryRemark = new FloristBusinessImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_order_history_detail);
		Controller.updateActInstance(this);

		Intent intent = getIntent();
		id_transaction = intent.hasExtra("id_transaction") ? intent
				.getStringExtra("id_transaction") : "0";
		title = intent.hasExtra("title") ? intent.getStringExtra("title")
				: "Order Detail";
		delivery_address = intent.hasExtra("delivery_address") ? intent
				.getStringExtra("delivery_address") : "Delivery Address";
		delivery_time = intent.hasExtra("delivery_time") ? intent
				.getStringExtra("delivery_time") : "Delivery Time";
		remark = intent.hasExtra("remark") ? intent.getStringExtra("remark")
				: "";
		initView();
		initData();
	}

	private void initView() {
		initCommonView();
		setHeaderTitle(title);
		detailAddress = (TextView) findViewById(R.id.order_detail_address);
		detailAddress.setText(delivery_address);
		detailAddress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SimpleToast.info(OrderHistoryDetailActivity.this,
						delivery_address);
			}
		});

		detailTime = (TextView) findViewById(R.id.order_detail_time);
		detailTime.setText(delivery_time);

		imvAddAlltoCart = (ImageView) findViewById(R.id.ivAddtoCart);
		imvAddAlltoCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < listItems.size(); i++) {
					OrderHistoryDetail item = listItems.get(i);
					if (item.getStatus().equalsIgnoreCase("available")) {
						String specReq = item.getSpecReq();
						String timeSlot = "9 - 12 pm";
						// double base_price = item.getSellingPrice() /
						// Integer.valueOf(item.getAmount());
						double base_price = item.getSellingPrice();
						// DetailItem detailItem = new
						// DetailItem(item.getItem_id(),
						// String.valueOf(item.getSellingPrice()),
						// item.getItemName(), item.getImg_path(),
						// item.getMain_color());
						// detailItem.setCode_name(item.getItemName());
						// OrderItem orderItem = new OrderItem(detailItem,
						// item.getItem_id(), Integer.valueOf(item.getAmount()),
						// item.getSellingPrice(), base_price,
						// item.getAddress(), specReq, timeSlot);
						// GlobalData.orderItem = orderItem;
						// GlobalData.listOrderItem.add(orderItem);

						boolean isExist = false;
						int p = 0;
						if (PQT.isListDirty(GlobalData.listOrderItem)) {
							for (OrderItem item_in : GlobalData.listOrderItem) {
								p++;
								if (item_in.getId().equalsIgnoreCase(
										item.getItem_id())) {
									isExist = true;
									break;
								}
							}
						}
						int editPosition = p - 1;
						if (isExist) {
							DetailItem detailItem = new DetailItem(item
									.getItem_id(), String.valueOf(item
									.getSellingPrice()), item.getItemName(),
									item.getImg_path(), item.getMain_color());
							OrderItem orderItem = new OrderItem(detailItem,
									item.getItem_id(), Integer.valueOf(item
											.getAmount()), item
											.getSellingPrice(), base_price,
									item.getAddress(), specReq, timeSlot);
							GlobalData.orderItem = orderItem;
							GlobalData.listOrderItem.set(editPosition,
									orderItem);
							SimpleToast.info(OrderHistoryDetailActivity.this,
									"Updated " + item.getAmount() + " "
											+ detailItem.getItem_name()
											+ "to cart");
						} else {
							DetailItem detailItem = new DetailItem(item
									.getItem_id(), String.valueOf(item
									.getSellingPrice()), item.getItemName(),
									item.getImg_path(), item.getMain_color());
							OrderItem orderItem = new OrderItem(detailItem,
									item.getItem_id(), Integer.valueOf(item
											.getAmount()), item
											.getSellingPrice(), base_price,
									item.getAddress(), specReq, timeSlot);
							GlobalData.orderItem = orderItem;
							GlobalData.listOrderItem.add(orderItem);
							SimpleToast.info(OrderHistoryDetailActivity.this,
									"Added " + item.getAmount() + " "
											+ detailItem.getItem_name()
											+ "to cart");
						}
					}
				}

				SimpleToast.info(OrderHistoryDetailActivity.this,
						"Added all items to cart");
			}
		});
		tvTotal = (TextView) findViewById(R.id.orderHistory_subTotal);
		lvOrderHistoryDetail = (ListView) findViewById(R.id.lvOrderHistoryDetail);
		listAdapter = new OrderDetailAdapter(this);
		edt_remark = (EditText) findViewById(R.id.edt_remark);
		edt_remark.setText(remark);
		txtUpdate = (TextView) findViewById(R.id.txtUpdate);
		txtUpdate.setVisibility(View.GONE);
		txtUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// updateOrderHistoryRemark(id_transaction,
				// edt_remark.getText().toString());
			}
		});
	}

	private void initData() {
		lvOrderHistoryDetail.setAdapter(listAdapter);
		getOrderHistoryDetail(id_transaction);
	}

	public void setTotal() {
		double sub = 0;

		for (int i = 0; i < listItems.size(); i++) {
			OrderHistoryDetail item = listItems.get(i);
			sub += item.getSellingPrice() * item.getAmount();
		}

		tvTotal.setText("S$ " + Functions.round2String(sub));
	}

	private void getOrderHistoryDetail(String transId) {
		doGetOrderHistoryDetail
				.setBusinessListener(new FloristBusinessListener() {

					@Override
					public void onErrorData(final ErrorMessage error_message) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								hideProgressBar();
								if (!error_message.getError_message()
										.equalsIgnoreCase(""))
									SimpleToast.error(
											Controller
													.getActInstance(MainActivity.class),
											error_message.getError_message());
								else {
									SimpleToast.error(
											Controller
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
									JSONObject root = object
											.getJSONObject("root");
									JSONArray data = root.getJSONArray("data");
									for (int i = 0; i < data.length(); i++) {
										OrderHistoryDetail item = new OrderHistoryDetail(
												data.getJSONObject(i));
										listItems.add(item);
									}
									listAdapter.setListItem(listItems);
									listAdapter.notifyDataSetChanged();

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									hideProgressBar();
									setTotal();
								}
							}
						});
					}
				});
		doGetOrderHistoryDetail.getOrderHistoryDetail(transId);
	}

	private void updateOrderHistoryRemark(String transId, String remark) {
		doUpdateOrderHistoryRemark
				.setBusinessListener(new FloristBusinessListener() {

					@Override
					public void onErrorData(final ErrorMessage error_message) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								hideProgressBar();
								if (!error_message.getError_message()
										.equalsIgnoreCase(""))
									SimpleToast.error(
											Controller
													.getActInstance(MainActivity.class),
											error_message.getError_message());
								else {
									SimpleToast.error(
											Controller
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
									JSONObject root = object
											.getJSONObject("root");
									String status = root.getString("status");
									if (status.equalsIgnoreCase("success")) {
										SimpleToast.ok(
												Controller
														.getActInstance(MainActivity.class),
												"Update remark successfully.");
									}

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									hideProgressBar();
									setTotal();
								}
							}
						});
					}
				});
		doUpdateOrderHistoryRemark.updateOrderHistoryRemark(transId, remark);
	}
}
