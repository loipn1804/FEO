package com.fareastorchid.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnOrderListener;
import com.fareastorchid.listener.OnSpecialRequestListener;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.StaticFunction;
import com.fareastorchid.view.ConfirmDialog;
import com.fareastorchid.view.ConfirmDialog.ConfirmButtonPressListener;

public class ReserveAdapter extends BaseAdapter implements ConfirmButtonPressListener {

	public LayoutInflater inflater;

	private List<OrderItem> listItems;

	private OnOrderListener listener;

	private OnSpecialRequestListener specialRequestListener;

	private Activity activity;
	private ConfirmDialog confirmDialog;

	public ReserveAdapter(Activity context) {
		super();
		this.activity = context;
		this.specialRequestListener = (OnSpecialRequestListener) this.activity;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = (OnOrderListener) context;
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
				convertView = inflater.inflate(R.layout.view_reserve_item, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.reserve_detail_name);
				holder.tvColor = (TextView) convertView.findViewById(R.id.reserve_detail_color);
				holder.tvSpecReq = (TextView) convertView.findViewById(R.id.reserve_detail_request);
				holder.tvCount = (TextView) convertView.findViewById(R.id.reserve_detail_count);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.reserve_detail_price);
				holder.ivItem = (ImageView) convertView.findViewById(R.id.reserve_detail_img);
				holder.btnDelete = (ImageView) convertView.findViewById(R.id.reserve_detail_delete);
				holder.reserve_detail_cover = (RelativeLayout) convertView.findViewById(R.id.reserve_detail_cover);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (listItems != null && position >= 0 && position < listItems.size()) {
				final OrderItem item = (OrderItem) listItems.get(position);
				if (item != null) {
					holder.tvName.setText(item.getItem().getItem_name());
					holder.tvColor.setText("Colour: " + item.getItem().getMain_color());
					holder.tvSpecReq.setText("Special request: " + item.getSpecReq());
					holder.tvCount.setText(item.getQuantity() + " x " + "S$ " + Functions.round2String(item.getSellingPrice()));
					holder.tvPrice.setText("S$ " + Functions.round2String(item.getSellingPrice() * item.getQuantity()));

					if (!item.getItem().getImg_path().equalsIgnoreCase("")) {
						String imgUrl = UrlHelper.FLORIST_IMG + item.getItem().getImg_path();
						imgUrl = imgUrl.replace(" ", "%20");
						//UrlImageViewHelper.setUrlDrawable(holder.ivItem, imgUrl, R.drawable.ic_loading);
						
						imgUrl = StaticFunction.checkLink(imgUrl);
						StaticFunction.getImageLoader().displayImage(imgUrl, holder.ivItem, StaticFunction.getImageLoaderOptions());
					}

					holder.reserve_detail_cover.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							specialRequestListener.onUpdateSpecialRequest(position);
						}
					});

					holder.btnDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							confirmDialog = new ConfirmDialog(activity, ReserveAdapter.this, position, "Notice", "Do you want to remove this item out of cart?");
							confirmDialog.show();
						}
					});
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
		public TextView tvSpecReq;
		public TextView tvCount;
		public TextView tvPrice;
		public ImageView btnDelete;
		public RelativeLayout reserve_detail_cover;
	}
}
