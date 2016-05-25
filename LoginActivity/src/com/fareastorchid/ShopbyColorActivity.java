package com.fareastorchid;

import android.os.Bundle;
import android.widget.ListView;

import com.fareastorchid.adapter.ColorAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.Color;
import com.fareastorchid.model.controller.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pierry.simpletoast.SimpleToast;

public class ShopbyColorActivity extends AbstractActivity {

    private ColorAdapter colorAdapter;
    private List<Color> lsColors = new ArrayList<Color>();
    private ListView lvColor;
    private FloristBusiness doGetAllColor = new FloristBusinessImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopbycolor);
        Controller.updateActInstance(this);

        GlobalData.shouldGetItemByColor = true;
        initView();
        initData();
    }

    private void initView() {
        initCommonView();
        setHeaderTitle("Main Colors");

        lvColor = (ListView) findViewById(R.id.lvColor);
        colorAdapter = new ColorAdapter(this);
    }

    private void initData() {
        lvColor.setAdapter(colorAdapter);
        getAllColor();
    }

    private void getAllColor() {
        doGetAllColor.setBusinessListener(new FloristBusinessListener() {

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
                            JSONArray colorJSONArray = root.getJSONArray("data");
                            if (colorJSONArray.length() != 0) {
                                try {
                                    // don't add cateId 0
                                    for (int i = 1; i < colorJSONArray.length(); i++) {
                                        JSONObject data = colorJSONArray.getJSONObject(i);
                                        Color color = new Color(data);
                                        lsColors.add(color);
                                    }
                                    colorAdapter.setListItem(lsColors);
                                    colorAdapter.notifyDataSetChanged();

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
        doGetAllColor.getAllColor();
    }
}