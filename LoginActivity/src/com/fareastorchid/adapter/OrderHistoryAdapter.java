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

import com.fareastorchid.OrderHistoryDetailActivity;
import com.fareastorchid.R;
import com.fareastorchid.model.OrderHistory;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;

import java.util.List;

public class OrderHistoryAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private Activity activity;
    private List<OrderHistory> listItems;

    public OrderHistoryAdapter(Activity context) {
        super();
        this.activity = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<OrderHistory> getListItem() {
        return listItems;
    }

    public void setListItem(List<OrderHistory> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    @Override
    public OrderHistory getItem(int position) {
        if (listItems != null)
            return listItems.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (listItems != null)
            return Long.parseLong(listItems.get(position).getOrder_no());
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        try {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_order_history_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.historyName);
                holder.tvNo = (TextView) convertView.findViewById(R.id.historyNo);
                holder.tvAmount = (TextView) convertView.findViewById(R.id.historyAmount);
                holder.tvYear = (TextView) convertView.findViewById(R.id.history_detail_year);
                holder.tvDay = (TextView) convertView.findViewById(R.id.history_detail_day);
                holder.tvMonth = (TextView) convertView.findViewById(R.id.history_detail_month);
                holder.tvTime = (TextView) convertView.findViewById(R.id.historyTimeSlot);
                holder.rlShipment = (LinearLayout) convertView.findViewById(R.id.historyLayout);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (listItems != null && position >= 0 && position < listItems.size()) {
                final OrderHistory item = (OrderHistory) listItems.get(position);
                if (item != null) {
                    if (!item.getContact_person().equalsIgnoreCase("")) {
                        holder.tvName.setText(item.getContact_person());
                    }
                    if (!item.getOrder_no().equalsIgnoreCase("")) {
                        holder.tvNo.setText("Order Number: " + item.getOrder_no());
                    }
                    if (item.getTotal_amount() != 0) {
                        holder.tvAmount.setText("S$ " + Functions.round2String(item.getTotal_amount()));
                    }
                    if (!item.getDate().equalsIgnoreCase("")) {
                        String year = item.getDate().substring(0, 4);
                        String day = item.getDate().substring(8, 10);
                        String month = PQT.numToMonth2(Integer.valueOf(item.getDate().substring(5, 7)));
                        holder.tvYear.setText(year);
                        holder.tvDay.setText(day);
                        holder.tvMonth.setText(month);

                    }
                    if (!item.getDelivery_time().equalsIgnoreCase("")) {
                        holder.tvTime.setText("Timeslot: " + item.getDelivery_time());
                    }
                    holder.rlShipment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String day = item.getDate().substring(8, 10);
                            String month = PQT.numToMonth(Integer.valueOf(item.getDate().substring(5, 7)));
                            OrderHistory item = getItem(position);
                            Intent i = new Intent(activity, OrderHistoryDetailActivity.class);
                            i.putExtra("id_transaction", item.getOrder_no());
                            i.putExtra("title", day + " " + month);
                            i.putExtra("delivery_address", item.getDelivery_address());
                            i.putExtra("delivery_time", item.getDelivery_time());
                            i.putExtra("remark", item.getRemark());
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
        public TextView tvNo;
        public TextView tvName;
        public TextView tvYear;
        public TextView tvDay;
        public TextView tvMonth;
        public TextView tvAmount;
        public TextView tvTime;
    }

}
