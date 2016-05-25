package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.model.Day;

import java.util.List;

public class DeliveryDaysAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<Day> listDays;


    public DeliveryDaysAdapter(Activity context) {
        super();
        this.activity = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Day> getListDay() {
        return listDays;
    }

    public void setListDays(List<Day> listDays) {
        this.listDays = listDays;
    }

    @Override
    public int getCount() {
        if (listDays != null)
            return listDays.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listDays != null)
            return listDays.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        try {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.delivery_item_date, null);
                holder.item = (RelativeLayout) convertView.findViewById(R.id.item_date);
                holder.tvDate = (TextView) convertView.findViewById(R.id.del_date_day);
                holder.tvMonth = (TextView) convertView.findViewById(R.id.del_date_month);
                holder.tvWeek = (TextView) convertView.findViewById(R.id.del_week);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (listDays != null && position >= 0 && position < listDays.size()) {
                final Day day = listDays.get(position);
                if (day != null) {
                    if (!day.getDay_no().equalsIgnoreCase("")) {
                        holder.tvDate.setText(day.getDay_no());
                    }
                    if (!day.getMonth().equalsIgnoreCase("")) {
                        holder.tvMonth.setText(day.getMonth());
                    }
                    if (!day.getWeek().equalsIgnoreCase("")) {
                        holder.tvWeek.setText(day.getWeek());
                    }
                    if (day.isState()) {
                        holder.tvDate.setTextColor(activity.getResources().getColor(R.color.orange));
                        holder.tvMonth.setTextColor(activity.getResources().getColor(R.color.orange));
                        holder.tvWeek.setTextColor(activity.getResources().getColor(R.color.orange));
                    } else {
                        holder.tvDate.setTextColor(activity.getResources().getColor(R.color.black));
                        holder.tvMonth.setTextColor(activity.getResources().getColor(R.color.gray_text));
                        holder.tvWeek.setTextColor(activity.getResources().getColor(R.color.black));
                    }
                }
                holder.item.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        resetState();
                        day.setState(true);
                        notifyDataSetChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void resetState() {
        for (int i = 0; i < listDays.size(); i++) {
            Day day = listDays.get(i);
            day.setState(false);
        }
    }

    public static class ViewHolder {
        public RelativeLayout item;
        public TextView tvDate;
        public TextView tvMonth;
        public TextView tvWeek;
    }

}
