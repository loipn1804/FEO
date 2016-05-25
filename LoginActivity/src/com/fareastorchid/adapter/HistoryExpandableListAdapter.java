package com.fareastorchid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fareastorchid.ForecastGridAdapter;
import com.fareastorchid.R;
import com.fareastorchid.model.Forecast;
import com.fareastorchid.model.ForecastCountry;
import com.fareastorchid.model.ShipmentItem;
import com.fareastorchid.util.PQT;
import com.fareastorchid.view.UtilityCustomGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter for displaying history, splitted in bins. Adapted from:
 * https:/
 * /github.com/CyanogenMod/android_packages_apps_Browser/blob/gingerbread/
 * src/com/android/browser/BrowserHistoryPage.java
 * http://grepcode.com/file/repository
 * .grepcode.com/java/ext/com.google.android/android-apps/
 * 2.2_r1.1/com/android/browser/DateSortedExpandableListAdapter.java/?v=source
 */
public class HistoryExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater = null;

    private Activity activity;
    private List<Forecast> listItems;

    /**
     * Constructor.
     *
     * @param context   The current context.
     * @param cursor    The data cursor.
     * @param dateIndex The date index ?
     */
    public HistoryExpandableListAdapter(Activity context) {
        activity = context;

        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Forecast> getListItem() {
        return listItems;
    }

    public void setListItem(List<Forecast> listItems) {
        this.listItems = listItems;
    }

    /**
     * Create a new child view.
     *
     * @return The created view.
     */
    private View getCustomChildView() {
        View view = mInflater.inflate(R.layout.history_row, null, false);

        return view;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = getCustomChildView();

        UtilityCustomGridView gvForecast = (UtilityCustomGridView) view.findViewById(R.id.gv_forecast_detail);

        List<ShipmentItem> countries = new ArrayList<ShipmentItem>();
        if (getGroup(groupPosition).getListCountries() != null) {
            for (ForecastCountry country : getGroup(groupPosition).getListCountries()) {
                ShipmentItem item = new ShipmentItem(getGroup(groupPosition).getDateArrival(), country.getCode(), country.getName());
                countries.add(item);
            }
        }
        ForecastGridAdapter adapter = new ForecastGridAdapter(activity);
        adapter.setListItem(countries);
        adapter.notifyDataSetChanged();
        gvForecast.setAdapter(adapter);

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Forecast getGroup(int groupPosition) {
        if (listItems != null)
            return listItems.get(groupPosition);
        return null;
    }

    @Override
    public int getGroupCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_forecast_group, null);
        }

        TextView tvDay = (TextView) convertView.findViewById(R.id.fore_item_day);
        if (!getGroup(groupPosition).getDateArrival().equalsIgnoreCase("")) {
            String year = getGroup(groupPosition).getDateArrival().substring(0, 4);
            String day = getGroup(groupPosition).getDateArrival().substring(8, 10);
            String month = PQT.numToMonth(Integer.valueOf(getGroup(groupPosition).getDateArrival().substring(5, 7)));
            tvDay.setText(day + month + year);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}