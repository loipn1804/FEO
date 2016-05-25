package com.fareastorchid;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.fareastorchid.adapter.HistoryExpandableListAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.model.Forecast;
import com.fareastorchid.model.controller.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pierry.simpletoast.SimpleToast;

public class ForecastActivity extends AbstractActivity {

    private ExpandableListView lvForecast;
    // private ForecastListAdapter2 listAdapter;
    private HistoryExpandableListAdapter expListAdapter;
    private List<Forecast> listForecast = new ArrayList<Forecast>();
    private FloristBusiness doForecast = new FloristBusinessImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_forecast);
        Controller.updateActInstance(this);
        initView();
        initData();

    }

    private void initView() {
        initCommonView();
        setHeaderTitle("Shipment Forecasts");
        lvForecast = (ExpandableListView) findViewById(R.id.expandListForecast);
        expListAdapter = new HistoryExpandableListAdapter(this);
    }

    private void initData() {

        lvForecast.setAdapter(expListAdapter);
        getForecast();
    }

    private void getForecast() {
        doForecast.setBusinessListener(new FloristBusinessListener() {

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
                            JSONArray data = root.getJSONArray("data");
                            if (data.length() != 0) {
                                try {
                                    for (int i = 0; i < data.length(); i++) {
                                        Forecast item = new Forecast(data.getJSONObject(i));
                                        listForecast.add(item);
                                    }
                                    expListAdapter.setListItem(listForecast);
                                    expListAdapter.notifyDataSetChanged();

                                    int totalItem = expListAdapter.getGroupCount();

                                    while (totalItem >= 0) {
                                        lvForecast.expandGroup(--totalItem);
                                    }
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
        doForecast.getForecastList();
    }

}