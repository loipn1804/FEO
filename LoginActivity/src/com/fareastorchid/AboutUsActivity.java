package com.fareastorchid;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.fareastorchid.model.controller.Controller;

public class AboutUsActivity extends AbstractActivity implements OnClickListener {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        Controller.updateActInstance(this);
        initView();
    }

    private void initView() {
        initCommonView();
        setHeaderTitle("About Us");

//		webView = (WebView) findViewById(R.id.activity_aboutus_webview);
//		webView.loadUrl("file:///android_asset/aboutus.html");
    }

    @Override
    public void onClick(View arg0) {
    }
}
