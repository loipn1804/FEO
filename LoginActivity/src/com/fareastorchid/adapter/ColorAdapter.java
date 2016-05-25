package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fareastorchid.MainActivity;
import com.fareastorchid.R;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.Color;

import java.util.List;

public class ColorAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<Color> listItems;

    public ColorAdapter(Activity context) {
        super();
        this.activity = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Color> getListItem() {
        return listItems;
    }

    public void setListItem(List<Color> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    @Override
    public Color getItem(int position) {
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
                convertView = inflater.inflate(R.layout.view_shipment_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.country_name);
                holder.rlShipment = (LinearLayout) convertView.findViewById(R.id.rlShipment);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (listItems != null && position >= 0 && position < listItems.size()) {
                final Color item = (Color) listItems.get(position);
                if (item != null) {
                    if (!item.getMain_color().equalsIgnoreCase("")) {
                        holder.tvName.setText(item.getMain_color());
                    }
                    holder.rlShipment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Color item = getItem(position);

                            GlobalData.shouldGetMainCat = true;
                            GlobalData.currentType = GlobalData.TYPE_COLOR;
                            GlobalData.currentColorId = Integer.valueOf(item.getId());
                            Intent i = new Intent(activity, MainActivity.class);
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
        public LinearLayout rlShipment;
        public TextView tvName;

    }
}
