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
import com.fareastorchid.model.Discount;
import com.fareastorchid.util.Functions;

import java.util.List;

public class DiscountAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private List<Discount> listDiscounts;
    private double basePrice;

    public DiscountAdapter(Activity context) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Discount> getListDiscount() {
        return listDiscounts;
    }

    public void setListDiscounts(List<Discount> listDiscounts, double basePrice) {
        this.listDiscounts = listDiscounts;
        this.basePrice = basePrice;
    }

    @Override
    public int getCount() {
        if (listDiscounts != null)
            return listDiscounts.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listDiscounts != null)
            return listDiscounts.get(position);
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
                convertView = inflater.inflate(R.layout.view_discount, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.dc_name);
                holder.tvAmount = (TextView) convertView.findViewById(R.id.dc_amount);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (listDiscounts != null && position >= 0 && position < listDiscounts.size()) {
                final Discount discount = listDiscounts.get(position);
                if (discount != null) {
                    if (!discount.getName().equalsIgnoreCase("")) {
                        holder.tvName.setText(discount.getTotal_item() + " and above");
                    }

                    if (!discount.getAmount().equalsIgnoreCase("")) {
                        holder.tvAmount.setText("S$ " + Functions.round2String(basePrice - Double.valueOf(discount.getAmount())));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
        public TextView tvAmount;
    }
}
