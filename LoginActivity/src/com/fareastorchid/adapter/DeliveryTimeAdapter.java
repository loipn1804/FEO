package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.model.Time;

import java.util.List;

public class DeliveryTimeAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<Time> listTimes;

    public DeliveryTimeAdapter(Activity context) {
        super();
        this.activity = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Time> getListTime() {
        return listTimes;
    }

    public void setListTimes(List<Time> listTimes) {
        this.listTimes = listTimes;
    }

    @Override
    public int getCount() {
        if (listTimes != null)
            return listTimes.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listTimes != null)
            return listTimes.get(position);
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
                convertView = inflater.inflate(R.layout.delivery_item_time, null);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tvTimes);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (listTimes != null && position >= 0 && position < listTimes.size()) {
                final Time time = listTimes.get(position);
                if (time != null) {
                    if (!time.getTime().equalsIgnoreCase("")) {
                        holder.tvTime.setText(time.getTime());
                    }
                    if (time.isState()) {
                        holder.tvTime.setTextColor(activity.getResources().getColor(R.color.orange));
                    } else {
                        holder.tvTime.setTextColor(activity.getResources().getColor(R.color.black));
                    }
                }
                holder.tvTime.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        resetState();
                        time.setState(true);
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
        for (int i = 0; i < listTimes.size(); i++) {
            Time time = listTimes.get(i);
            time.setState(false);
        }
    }

    public static class ViewHolder {
        public TextView tvTime;
    }

}