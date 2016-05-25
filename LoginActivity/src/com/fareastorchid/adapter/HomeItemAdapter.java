package com.fareastorchid.adapter;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fareastorchid.FlowerDetailActivity;
import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.HomeItem;
import com.fareastorchid.util.StaticFunction;
import com.joooonho.SelectableRoundedImageView;

public class HomeItemAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<HomeItem> listItems;
	private ViewHolder holder;

	public HomeItemAdapter(Activity context) {
		super();
		this.activity = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<HomeItem> getListItem() {
		return listItems;
	}

	public void setListItem(List<HomeItem> listItems) {
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		if (listItems != null)
			return listItems.size();
		return 0;
	}

	@Override
	public HomeItem getItem(int position) {
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
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.view_item_detail, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvItemName);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvItemPrice);
				holder.ivItem = (SelectableRoundedImageView) convertView.findViewById(R.id.ivItem);
				holder.lnCover = (RelativeLayout) convertView.findViewById(R.id.view_item_ln_cover);
				holder.tvPriceTitle = (TextView) convertView.findViewById(R.id.tvPriceTitle);
				holder.ivItemStatus = (TextView) convertView.findViewById(R.id.view_item_tv_itemstatus);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (listItems != null && position >= 0 && position < listItems.size()) {
				HomeItem item = listItems.get(position);
				if (item != null) {
					holder.tvName.setText(item.getName());

					if (GlobalData.isLogin) {
						holder.tvPriceTitle.setText("Trader Price: ");
						holder.tvPrice.setText("S$ " + item.getTraderPrice());
					} else {
						holder.tvPriceTitle.setText("Wholesale Price: ");
						holder.tvPrice.setText("S$ " + item.getWholesalePrice());
					}

					if (!item.getUrl().equals("")) 
					{
						String itemUrl = item.getUrl();
						if (!item.getUrl().substring(0, 1).equalsIgnoreCase("/")) 
						{
							itemUrl = "/" + itemUrl;
						}
															
						String imgUrl = UrlHelper.FLORIST_IMG + itemUrl;
						imgUrl = imgUrl.replace(" ", "%20");
						
						imgUrl = StaticFunction.checkLink(imgUrl);
											
						Log.e("image", imgUrl);
						//UrlImageViewHelper.setUrlDrawable(holder.ivItem, imgUrl, R.drawable.ic_loading);
						StaticFunction.getImageLoader().displayImage(imgUrl, holder.ivItem, StaticFunction.getImageLoaderOptions());
					}

					holder.ivItemStatus.setVisibility(View.VISIBLE);
					if (item.getStatus().equalsIgnoreCase("sold out")) {
						holder.ivItemStatus.setBackgroundResource(R.drawable.feo_main_item_status_soldout);
						holder.ivItemStatus.setText(item.getStatus().toUpperCase(Locale.ENGLISH));
					} else if (item.getListColors() != null && item.getListColors().size() > 1) {
						holder.ivItemStatus.setBackgroundResource(R.drawable.new_color);
					} else {
						holder.ivItemStatus.setVisibility(View.GONE);
					}

					holder.lnCover.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							HomeItem item = getItem(position);
							GlobalData.listColor = item.getListColors();
							Intent i = new Intent(activity, FlowerDetailActivity.class);
							i.putExtra("itemId", item.getId());
							i.putExtra("item_status", item.getStatus());
							i.putExtra("cat_name", item.getCat_name());
							i.putExtra("trader_price", item.getTraderPrice());
							i.putExtra("quantity", Integer.valueOf(item.getQuantity()));
							activity.startActivity(i);
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public RelativeLayout lnCover;
		public TextView tvName;
		public SelectableRoundedImageView ivItem;
		public TextView ivItemStatus;
		public TextView tvPrice;
		public TextView tvPriceTitle;
	}
}
