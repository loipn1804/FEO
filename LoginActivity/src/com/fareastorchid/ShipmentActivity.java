package com.fareastorchid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.fareastorchid.adapter.ShipmentListAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.ForecastCountry;
import com.fareastorchid.model.ShipmentItem;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pierry.simpletoast.SimpleToast;

public class ShipmentActivity extends AbstractActivity {

    private ListView lvShipment;
    private ShipmentListAdapter listAdapter;
    private List<ShipmentItem> lsShipmentItems = new ArrayList<ShipmentItem>();
    private boolean isFromForecast = false;
    private String date_arrival = "";
    private TextView tvTitle;
    private FloristBusiness doGetAllCountry = new FloristBusinessImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipmen);
        Controller.updateActInstance(this);

        Intent intent = getIntent();
        isFromForecast = intent.getBooleanExtra("isFromForecast", false);
        if (intent.hasExtra("forecastDate"))
            date_arrival = intent.getStringExtra("forecastDate");
        initView();
        initData();
    }

    private void initView() {
        initCommonView();

        setHeaderTitle("Country List");
        lvShipment = (ListView) findViewById(R.id.lvShipment);
        tvTitle = (TextView) findViewById(R.id.shipment_select);

        tvTitle.setText(String.format("Select a country for %s",
                Functions.formatDateTime(date_arrival)));
        listAdapter = new ShipmentListAdapter(this, date_arrival);
    }

    private void initData() {
        lvShipment.setAdapter(listAdapter);
        if (!isFromForecast)
            getAllCountry();
        else {
            if (GlobalData.listCountries != null) {
                for (int i = 0; i < GlobalData.listCountries.size(); i++) {
                    ForecastCountry country = GlobalData.listCountries.get(i);
                    ShipmentItem item = new ShipmentItem(
                            country.getId_forecast(), country.getCode(),
                            country.getName());
                    lsShipmentItems.add(item);
                }
                listAdapter.setType(GlobalData.TYPE_FORECAST);
                listAdapter.setListItem(lsShipmentItems);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getAllCountry() {
        doGetAllCountry.setBusinessListener(new FloristBusinessListener() {

            @Override
            public void onErrorData(final ErrorMessage error_message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        hideProgressBar();
                        if(!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(MainActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(MainActivity.class), R.string.err_connect);
						}
                    }
                });
            }

            @Override
            public void onPreProcessed() {
                showProgressBar();
            }

            @Override
            public void onDataProcessed(final Object entity) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject object = (JSONObject) entity;
                            JSONObject root = object.getJSONObject("root");
                            JSONArray catTypeJSONArray = root
                                    .getJSONArray("data");
                            if (catTypeJSONArray.length() != 0) {
                                try {
                                    for (int i = 0; i < catTypeJSONArray
                                            .length(); i++) {
                                        JSONObject data = catTypeJSONArray
                                                .getJSONObject(i);
                                        ShipmentItem item = new ShipmentItem(
                                                data);
                                        lsShipmentItems.add(item);
                                    }
                                    listAdapter.setType(GlobalData.TYPE_NORMAL);
                                    listAdapter.setListItem(lsShipmentItems);
                                    listAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } finally {
                            hideProgressBar();
                        }
                    }
                });
            }
        });
        doGetAllCountry.getAllCountry();
    }
}
