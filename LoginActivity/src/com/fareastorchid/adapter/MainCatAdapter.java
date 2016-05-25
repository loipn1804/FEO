package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.listener.OnFloristListener;
import com.fareastorchid.model.MainCat;

import java.util.List;

public class MainCatAdapter extends BaseAdapter {
    public LayoutInflater inflater;

    private List<MainCat> listCates;

    private OnFloristListener listener;

    public MainCatAdapter(Context context) {
        super();
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = (OnFloristListener) context;
    }

    public List<MainCat> getListCates() {
        return listCates;
    }

    public void setListCates(List<MainCat> listCates) {
        this.listCates = listCates;
    }

    @Override
    public int getCount() {
        if (listCates != null)
            return listCates.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listCates != null)
            return listCates.get(position);
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
                convertView = inflater.inflate(R.layout.view_panel_item, null);
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.tv_CatName);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (listCates != null && position >= 0
                    && position < listCates.size()) {
                final MainCat cate = listCates.get(position);
                if (cate != null) {
                    if (!cate.getCat_name().equalsIgnoreCase("")) {
                        holder.tvName.setText(cate.getCat_name());
                    }
                    holder.tvName.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onChooseCatType(cate);
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
        public TextView tvName;
    }
}
