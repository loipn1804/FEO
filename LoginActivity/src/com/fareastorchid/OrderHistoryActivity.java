package com.fareastorchid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.OrderHistoryAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.model.OrderHistory;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Connectivity;

public class OrderHistoryActivity extends AbstractActivity {

    private GridView lvOrderHistory;
    private OrderHistoryAdapter listAdapter;
    private List<OrderHistory> lsOrderHistorys = new ArrayList<OrderHistory>();
    private int currentPage = 1;
    private boolean isLoading = true;
    private boolean isNoMoreData = false;
    private boolean fistLoad = true;
    private String userId = "";
    private FloristBusiness doGetOrderHistory = new FloristBusinessImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_order_history);
        Controller.updateActInstance(this);
        Intent intent = getIntent();
        userId = intent.hasExtra("userId") ? intent.getStringExtra("userId") : "-1";
        initView();
        initData();
    }

    private void initView() {
        initCommonView();
        setHeaderTitle("Order History");
        lvOrderHistory = (GridView) findViewById(R.id.gvHistoryList);
        listAdapter = new OrderHistoryAdapter(this);
    }

    private void initData() {

        lvOrderHistory.setAdapter(listAdapter);
        getOrderHistory(currentPage);
        lvOrderHistory.setOnScrollListener(new EndlessScrollListener());
    }

    private void getOrderHistory(int pageIndex) {
        doGetOrderHistory.setBusinessListener(new FloristBusinessListener() {

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
                        isLoading = false;
                    }
                });
            }

            @Override
            public void onPreProcessed() {
                showProgressBar();
                isLoading = true;
            }

            @Override
            public void onDataProcessed(final Object entity) {
                isLoading = false;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject object = (JSONObject) entity;
                            JSONObject root = object.getJSONObject("root");
                            JSONArray catTypeJSONArray = root.getJSONArray("data");
                            if (catTypeJSONArray.length() != 0) {
                                try {
                                    // don't add cateId 0
                                    for (int i = 0; i < catTypeJSONArray.length(); i++) {
                                        JSONObject data = catTypeJSONArray.getJSONObject(i);
                                        OrderHistory item = new OrderHistory(data);
                                        lsOrderHistorys.add(item);
                                    }
                                    listAdapter.setListItem(lsOrderHistorys);
                                    listAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                isNoMoreData = true;
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } finally {
                            hideProgressBar();
                            fistLoad = false;
                            currentPage++;
                        }
                    }
                });
            }
        });
        doGetOrderHistory.getOrderHistory(userId, pageIndex);
    }

    private class EndlessScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (isNoMoreData || fistLoad) {
                return;
            }

            if (totalItemCount >= visibleItemCount && !isLoading
                    // && totalItemCount < totalResult
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 3) && Connectivity.isConnected(OrderHistoryActivity.this)) {
                // Log.w("RetreiveListMovieTask","onScroll");
                // new RetreiveListMovieTask(Share.CurrentCategory, searchKey,
                // currentPage++).execute(getActivity());

                Log.w("EndlessScrollListener", firstVisibleItem + "" + visibleItemCount + "" + totalItemCount + currentPage);
                getOrderHistory(currentPage);
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            return;
        }
    }
}
