package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.model.ForecastItem;

import java.util.List;

public class ForecastItemByCountryAdapter extends BaseAdapter {
    public LayoutInflater inflater;

    private List<ForecastItem> listItems;

    public ForecastItemByCountryAdapter(Activity context) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ForecastItem> getListItem() {
        return listItems;
    }

    public void setListItem(List<ForecastItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    @Override
    public ForecastItem getItem(int position) {
        if (listItems != null)
            return listItems.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (listItems != null)
            return Long.parseLong(listItems.get(position).getId_forecast());
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_item_detail_noprice, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvItemName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (listItems != null && position >= 0 && position < listItems.size()) {
                final ForecastItem item = (ForecastItem) listItems.get(position);
                if (item != null) {
                    holder.tvName.setText(item.getItem_name());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
    }
}
