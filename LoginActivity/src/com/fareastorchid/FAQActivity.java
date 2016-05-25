package com.fareastorchid;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.fareastorchid.model.controller.Controller;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FAQActivity extends AbstractActivity implements OnClickListener {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Gillsansmt.ttf", R.attr.fontPath);
        setContentView(R.layout.activity_faq);
        
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/gilbold.ttf");
        ((TextView) findViewById(R.id.txtRetailOpen)).setTypeface(typeface);
        ((TextView) findViewById(R.id.txtOfficeOpen)).setTypeface(typeface);
        
        Controller.updateActInstance(this);
        initView();
    }

    private void initView() {
        initCommonView();
        setHeaderTitle("FAQ");
    }

    @Override
    public void onClick(View arg0) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
