package com.fareastorchid.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.DetailItem;
import com.fareastorchid.model.Discount;
import com.fareastorchid.model.OrderHistoryDetail;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;
import com.fareastorchid.util.StaticFunction;

public class OrderDetailAdapter extends BaseAdapter {
	public LayoutInflater inflater;
	private Activity activity;
	private List<OrderHistoryDetail> listItems;

	public OrderDetailAdapter(Activity context) {
		super();
		this.activity = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<OrderHistoryDetail> getListItem() {
		return listItems;
	}

	public void setListItem(List<OrderHistoryDetail> listItems) {
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		if (listItems != null)
			return listItems.size();
		return 0;
	}

	@Override
	public OrderHistoryDetail getItem(int position) {
		if (listItems != null)
			return listItems.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// Log.w("getItemId", "getItemId");
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		try {
			Log.w("getView", "getView");
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.view_order_history_detail_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.history_detail_name);
				holder.tvColor = (TextView) convertView.findViewById(R.id.history_detail_color);
				holder.tvSpecReq = (TextView) convertView.findViewById(R.id.history_detail_request);
				holder.tvCount = (TextView) convertView.findViewById(R.id.history_detail_count);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.history_detail_price);
				holder.ivAdd = (ImageView) convertView.findViewById(R.id.history_detail_add);
				holder.ivItem = (ImageView) convertView.findViewById(R.id.history_detail_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (listItems != null && position >= 0 && position < listItems.size()) {
				final OrderHistoryDetail item = (OrderHistoryDetail) listItems.get(position);
				if (item != null) {
					holder.tvName.setText(item.getItemName());
					holder.tvColor.setText("Colour: " + item.getMain_color());
					holder.tvSpecReq.setText("Special request: " + item.getSpecReq());
					holder.tvCount.setText(item.getAmount() + " x " + "S$ " + Functions.round2String(item.getSellingPrice()));
					holder.tvPrice.setText("S$ " + Functions.round2String(item.getSellingPrice() * item.getAmount()));

					if (!item.getImg_path().equalsIgnoreCase("")) {
						if (!item.getImg_path().startsWith("/"))
							item.setImg_path("/" + item.getImg_path());
						String imgUrl = UrlHelper.FLORIST_IMG + item.getImg_path();
						imgUrl = imgUrl.replace(" ", "%20");

						imgUrl = StaticFunction.checkLink(imgUrl);
						//UrlImageViewHelper.setUrlDrawable(holder.ivItem, imgUrl, R.drawable.ic_loading);
						StaticFunction.getImageLoader().displayImage(imgUrl, holder.ivItem, StaticFunction.getImageLoaderOptions());
					}

					if (item.getStatus().equalsIgnoreCase("sold out")) {
						holder.ivAdd.setBackgroundResource(R.drawable.bg_soldout);
						holder.ivAdd.setOnClickListener(null);
					} else if (item.getStatus().equalsIgnoreCase("hidden")) {
						holder.ivAdd.setBackgroundResource(R.drawable.bg_unavailable);
						holder.ivAdd.setOnClickListener(null);
					} else {
						Log.w("item Avaiable", "avaiable");
						holder.ivAdd.setBackgroundResource(R.drawable.bg_addcart1);
						holder.ivAdd.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								String specReq = item.getSpecReq();
								String timeSlot = "9 - 12 pm";
//								double base_price = item.getSellingPrice() / Integer.valueOf(item.getAmount());
								double base_price = item.getSellingPrice();
//								DetailItem detailItem = new DetailItem(item.getItem_id(), String.valueOf(item.getSellingPrice()), item.getItemName(), item.getImg_path(), item.getMain_color());
//								OrderItem orderItem = new OrderItem(detailItem, item.getItem_id(), Integer.valueOf(item.getAmount()), item.getSellingPrice(), base_price, item.getAddress(), specReq,
//										timeSlot);
//								GlobalData.orderItem = orderItem;
//								GlobalData.listOrderItem.add(orderItem);
								
								boolean isExist = false;
								int p = 0;
								if (PQT.isListDirty(GlobalData.listOrderItem)) {
									for (OrderItem item_in : GlobalData.listOrderItem) {
										p++;
										if (item_in.getId().equalsIgnoreCase(item.getItem_id())) {
											isExist = true;
											break;
										}
									}
								}
								int editPosition = p-1;
								if (isExist) {
									DetailItem detailItem = new DetailItem(item.getItem_id(), String.valueOf(item.getSellingPrice()), item.getItemName(), item.getImg_path(), item.getMain_color());
									OrderItem orderItem = new OrderItem(detailItem, item.getItem_id(), Integer.valueOf(item.getAmount()), item.getSellingPrice(), base_price, item.getAddress(), specReq,
											timeSlot);
									GlobalData.orderItem = orderItem;
									GlobalData.listOrderItem.set(editPosition, orderItem);
									SimpleToast.info(activity, "Updated " + item.getAmount() + " " + detailItem.getItem_name() + "to cart");
								} else {
									DetailItem detailItem = new DetailItem(item.getItem_id(), String.valueOf(item.getSellingPrice()), item.getItemName(), item.getImg_path(), item.getMain_color());
									OrderItem orderItem = new OrderItem(detailItem, item.getItem_id(), Integer.valueOf(item.getAmount()), item.getSellingPrice(), base_price, item.getAddress(), specReq,
											timeSlot);
									GlobalData.orderItem = orderItem;
									GlobalData.listOrderItem.add(orderItem);
									SimpleToast.info(activity, "Added " + item.getAmount() + " " + detailItem.getItem_name() + "to cart");
								}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView tvName;
		public ImageView ivItem;
		public TextView tvColor;
		public TextView tvSpecReq;
		public TextView tvCount;
		public TextView tvPrice;
		public TextView tvAddress;
		public ImageView ivAdd;
	}
}