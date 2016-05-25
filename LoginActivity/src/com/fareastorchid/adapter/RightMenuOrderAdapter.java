package com.fareastorchid.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.FlowerDetailActivity;
import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnOrderListener;
import com.fareastorchid.model.Discount;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.StaticFunction;
import com.fareastorchid.view.ConfirmDialog.ConfirmButtonPressListener;

public class RightMenuOrderAdapter extends BaseAdapter implements ConfirmButtonPressListener {
	public LayoutInflater inflater;
	private Activity activity;
	private List<OrderItem> listItems;

	private OnOrderListener listener;

	private boolean showDelete = false;

	private int OpenFrom = GlobalData.FROM_MAIN;

	public RightMenuOrderAdapter(Activity context) {
		super();
		this.activity = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = (OnOrderListener) context;
	}

	public void setOpenFrom(int from) {
		this.OpenFrom = from;
	}

	public void isShowDelete(boolean show) {
		this.showDelete = show;
	}

	public List<OrderItem> getListItem() {
		return listItems;
	}

	public void setListItem(List<OrderItem> listItems) {
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		if (listItems != null)
			return listItems.size();
		return 0;
	}

	@Override
	public OrderItem getItem(int position) {
		if (listItems != null)
			return listItems.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (listItems != null)
			if (!listItems.get(position).getId().equalsIgnoreCase(""))
				return Long.parseLong(listItems.get(position).getId());
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		try {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.view_sliding_menu_right_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.rightmenu_order_name);
				holder.tvColor = (TextView) convertView.findViewById(R.id.rightmenu_order_color);
				holder.tvCount = (TextView) convertView.findViewById(R.id.rightmenu_order_count);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.rightmenu_order_price);
				holder.ivItem = (ImageView) convertView.findViewById(R.id.rightmenu_order_img);
				holder.btnDelete = (ImageView) convertView.findViewById(R.id.rightmenu_order_delete);
				holder.rlCover = (RelativeLayout) convertView.findViewById(R.id.rightmenu_order_rl_cover);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (listItems != null && position >= 0 && position < listItems.size()) {
				final OrderItem item = (OrderItem) listItems.get(position);
				if (item != null) {
					holder.tvName.setText(item.getItem().getItem_name());
					holder.tvColor.setText("Colour: " + item.getItem().getMain_color());
					holder.tvCount.setText(item.getQuantity() + " x " + "S$ " + Functions.round2String(item.getSellingPrice()));
					holder.tvPrice.setText("S$ " + Functions.round2String(item.getSellingPrice() * item.getQuantity()));

					if (!item.getItem().getImg_path().equalsIgnoreCase("")) {
						String imgUrl = UrlHelper.FLORIST_IMG + item.getItem().getImg_path();
						imgUrl = imgUrl.replace(" ", "%20");
						//UrlImageViewHelper.setUrlDrawable(holder.ivItem, imgUrl, R.drawable.ic_loading);
						imgUrl = StaticFunction.checkLink(imgUrl);
						StaticFunction.getImageLoader().displayImage(imgUrl, holder.ivItem, StaticFunction.getImageLoaderOptions());
					}
					holder.btnDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String itemCode = GlobalData.listOrderItem.get(position).getItem().getCode_name();
							List<Discount> lsDiscount = GlobalData.listOrderItem.get(position).getItem().getLsDiscount();
							GlobalData.listOrderItem.remove(position);
							if (lsDiscount != null && lsDiscount.size() > 0) {
								GlobalData.updateDiscountPrice(itemCode);
							}
							setListItem(GlobalData.listOrderItem);
							notifyDataSetChanged();
							listener.onDeteleOrder();
							SimpleToast.info(activity, "Removed " + item.getItem().getItem_name());
						}
					});

					holder.rlCover.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (OpenFrom == GlobalData.FROM_MAIN) {
								GlobalData.listColor = item.getItem().getListColors();
								Intent i = new Intent(activity, FlowerDetailActivity.class);
								i.putExtra("editItem", true);
								i.putExtra("itemId", item.getId());
								i.putExtra("item_status", item.getItem().getItem_status());
								i.putExtra("quantity", item.getQuantity());
								i.putExtra("editPosition", position);
								activity.startActivity(i);
							} else {
								GlobalData.listColor = item.getItem().getListColors();
								GlobalData.editOrderItem = item;
								GlobalData.editPosition = position;
								listener.onViewOrder();
							}
						}
					});

					if (showDelete) {
						holder.btnDelete.setVisibility(View.VISIBLE);
					} else {
						holder.btnDelete.setVisibility(View.GONE);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public void onConfirmButtonPress(boolean isYes, int extraData) {
		if (isYes) {
			GlobalData.listOrderItem.remove(extraData);
			setListItem(GlobalData.listOrderItem);
			notifyDataSetChanged();
			listener.onDeteleOrder();
		}
	}

	public static class ViewHolder {
		public TextView tvName;
		public ImageView ivItem;
		public TextView tvColor;
		public TextView tvCount;
		public TextView tvPrice;
		public ImageView btnDelete;
		public RelativeLayout rlCover;
	}
}
