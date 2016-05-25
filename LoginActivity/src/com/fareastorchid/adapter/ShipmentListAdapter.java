package com.fareastorchid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fareastorchid.ForecastByCountryActivity;
import com.fareastorchid.MainActivity;
import com.fareastorchid.R;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.ShipmentItem;

import java.util.List;

public class ShipmentListAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private int type;
    private String date_arrival;

    private List<ShipmentItem> listItems;

    public ShipmentListAdapter(Activity context, String forecastDate) {
        super();
        this.activity = context;
        this.date_arrival = forecastDate;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ShipmentItem> getListItem() {
        return listItems;
    }

    public void setListItem(List<ShipmentItem> listItems) {
        this.listItems = listItems;
    }

    public void setType(int type) {
        this.type = type;
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
            return Long.parseLong(listItems.get(position).getId());
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        try {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_shipment_item,
                        null);
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.country_name);
                holder.rlShipment = (LinearLayout) convertView
                        .findViewById(R.id.rlShipment);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (listItems != null && position >= 0
                    && position < listItems.size()) {
                final ShipmentItem item = (ShipmentItem) listItems
                        .get(position);
                if (item != null) {
                    if (!item.getName().equalsIgnoreCase("")) {
                        holder.tvName.setText(item.getName());
                    }
                    holder.rlShipment
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ShipmentItem item = getItem(position);
                                    if (type == GlobalData.TYPE_FORECAST) {
                                        Intent i = new Intent(activity,
                                                ForecastByCountryActivity.class);
                                        i.putExtra("country_name",
                                                item.getName());
                                        i.putExtra("country_code",
                                                item.getCode());
                                        i.putExtra("date_arrival", date_arrival);
                                        activity.startActivity(i);
                                    } else {
                                        GlobalData.shouldGetMainCat = true;
                                        GlobalData.currentType = GlobalData.TYPE_COUNTRY;
                                        GlobalData.currentCountryId = Integer
                                                .valueOf(item.getId());
                                        Intent i = new Intent(activity,
                                                MainActivity.class);
                                        activity.startActivity(i);
                                    }

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
        public LinearLayout rlShipment;
        public TextView tvName;

    }
}
