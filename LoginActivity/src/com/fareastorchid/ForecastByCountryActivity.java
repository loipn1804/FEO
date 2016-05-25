package com.fareastorchid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fareastorchid.adapter.ForecastItemByCountryAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.model.ForecastItem;
import com.fareastorchid.model.controller.Controller;

public class ForecastByCountryActivity extends AbstractActivity {

	private ListView lvShopbyCountry;
	private ForecastItemByCountryAdapter listForeAdapter;
	private List<ForecastItem> lsForecastItems = new ArrayList<ForecastItem>();
	private String country_code;
	private String conntryName;
	private String date_arrival;
	private FloristBusiness doGetForecastDetail = new FloristBusinessImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopbycountry);
		Controller.updateActInstance(this);
		Intent intent = getIntent();

		country_code = intent.hasExtra("country_code") ? intent.getStringExtra("country_code") : "-1";
		conntryName = intent.hasExtra("country_name") ? intent.getStringExtra("country_name") : "Country";
		date_arrival = intent.hasExtra("date_arrival") ? intent.getStringExtra("date_arrival") : "";

		initView();
		initData();
	}

	private void initView() {
		initCommonView();
		lvShopbyCountry = (ListView) findViewById(R.id.lvShopbyCountry);
		listForeAdapter = new ForecastItemByCountryAdapter(this);
	}

	private void initData() {
		initCommonView();
		setHeaderTitle(conntryName);

		if (!country_code.equals("-1")) {
			lvShopbyCountry.setAdapter(listForeAdapter);
			getForecastDetail(country_code, date_arrival);
		}

	}

	private void getForecastDetail(String country_code, String date_arrival) {
		doGetForecastDetail.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
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
							JSONArray itemJSONArray = root.getJSONArray("data");
							if (itemJSONArray.length() != 0) {
								try {
									for (int i = 0; i < itemJSONArray.length(); i++) {
										JSONObject data = itemJSONArray.getJSONObject(i);
										ForecastItem item = new ForecastItem(data);
										lsForecastItems.add(item);
									}
									listForeAdapter.setListItem(lsForecastItems);
									listForeAdapter.notifyDataSetChanged();
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
		doGetForecastDetail.getForecastListDetail(country_code, date_arrival);
	}
}
