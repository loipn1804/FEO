package com.fareastorchid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fareastorchid.model.ShipmentItem;

import java.util.List;

public class ForecastGridAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<ShipmentItem> listItems;

    private ViewHolder holder;

    public ForecastGridAdapter(Activity context) {
        super();
        this.activity = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ShipmentItem> getListItem() {
        return listItems;
    }

    public void setListItem(List<ShipmentItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    @Override
    public ShipmentItem getItem(int position) {
        if (listItems != null)
            return listItems.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (listItems != null)
            return position;
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.forecast_grid_item, null);
            holder.tvCountry = (TextView) convertView.findViewById(R.id.tv_fore_item_gv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCountry.setText(getItem(position).getName());
        holder.tvCountry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShipmentItem item = getItem(position);
                Intent i = new Intent(activity, ForecastByCountryActivity.class);
                i.putExtra("country_name", item.getName());
                i.putExtra("country_code", item.getCode());
                i.putExtra("date_arrival", item.getId());
                activity.startActivity(i);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvCountry;
    }
}
