package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fareastorchid.FlowerDetailActivity;
import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.HomeItem;
import com.fareastorchid.util.StaticFunction;

import java.util.List;

public class SearchItemAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<HomeItem> listItems;

    public SearchItemAdapter(Activity context) {
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
        final ViewHolder holder;
        try {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_item_search, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.search_item_name);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.search_item_price);
                holder.ivItem = (ImageView) convertView.findViewById(R.id.search_item_img);
                holder.lnCover = (LinearLayout) convertView.findViewById(R.id.search_item_cover);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (listItems != null && position >= 0 && position < listItems.size()) {
                final HomeItem item = listItems.get(position);
                if (item != null) {
                    if (!item.getName().equalsIgnoreCase("")) {
                        holder.tvName.setText(item.getName());
                    }

                    if (GlobalData.isLogin)
                        holder.tvPrice.setText("S$ " + item.getTraderPrice());
                    else
                        holder.tvPrice.setText("S$ " + item.getWholesalePrice());

                    if (!item.getUrl().equalsIgnoreCase("")) {
                        if (!item.getUrl().startsWith("/"))
                            item.setUrl("/" + item.getUrl());
                        String imgUrl = UrlHelper.FLORIST_IMG + item.getUrl();
                        imgUrl = imgUrl.replace(" ", "%20");
                        //UrlImageViewHelper.setUrlDrawable(holder.ivItem, imgUrl, R.drawable.ic_loading);
                        imgUrl = StaticFunction.checkLink(imgUrl);
                        StaticFunction.getImageLoader().displayImage(imgUrl, holder.ivItem, StaticFunction.getImageLoaderOptions());
                    }
                }
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
                    activity.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        public LinearLayout lnCover;
        public TextView tvName;
        public ImageView ivItem;
        public TextView tvPrice;

    }
}
