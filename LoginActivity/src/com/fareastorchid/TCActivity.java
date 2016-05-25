package com.fareastorchid;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.controller.Controller;

public class TCActivity extends AbstractActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_t_and_c);

		Controller.updateActInstance(this);
		initView();
	}

	private void initView() {
		initCommonView();
		setHeaderTitle("T&C");
		ImageView tc_delivery_detail = (ImageView) findViewById(R.id.tc_delivery_detail);
		final LinearLayout lo_tc1 = (LinearLayout) findViewById(R.id.layout_tc1);
		final TextView lo_tc2 = (TextView) findViewById(R.id.layout_tc2);
		final LinearLayout lo_tc3 = (LinearLayout) findViewById(R.id.layout_tc3);
		final LinearLayout lo_tc4 = (LinearLayout) findViewById(R.id.layout_tc4);
		final LinearLayout lo_tc5 = (LinearLayout) findViewById(R.id.layout_tc5);
		final LinearLayout lo_tc6 = (LinearLayout) findViewById(R.id.layout_tc6);
		final LinearLayout lo_tc7 = (LinearLayout) findViewById(R.id.layout_tc7);
		final TextView lo_tc8 = (TextView) findViewById(R.id.layout_tc8);
		final TextView lo_tc9 = (TextView) findViewById(R.id.layout_tc9);
		final LinearLayout lo_tc10 = (LinearLayout) findViewById(R.id.layout_tc10);
		final LinearLayout lo_tc11 = (LinearLayout) findViewById(R.id.layout_tc11);
		final LinearLayout lo_tc12 = (LinearLayout) findViewById(R.id.layout_tc12);
		final LinearLayout deliveryPolicy = (LinearLayout) findViewById(R.id.deliveryPolicy);
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

		if (GlobalData.isLogin)
			deliveryPolicy.setVisibility(View.VISIBLE);
		else
			deliveryPolicy.setVisibility(View.GONE);
		TextView tc1 = (TextView) findViewById(R.id.tc1);
		tc1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc1.getVisibility() == View.GONE) {
					lo_tc1.setVisibility(View.VISIBLE);
				} else {
					lo_tc1.setVisibility(View.GONE);
				}
			}
		});
		TextView tc2 = (TextView) findViewById(R.id.tc2);
		tc2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc2.getVisibility() == View.GONE) {
					lo_tc2.setVisibility(View.VISIBLE);
				} else {
					lo_tc2.setVisibility(View.GONE);
				}
			}
		});
		TextView tc3 = (TextView) findViewById(R.id.tc3);
		tc3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc3.getVisibility() == View.GONE) {
					lo_tc3.setVisibility(View.VISIBLE);
				} else {
					lo_tc3.setVisibility(View.GONE);
				}
			}
		});
		TextView tc4 = (TextView) findViewById(R.id.tc4);
		tc4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc4.getVisibility() == View.GONE) {
					lo_tc4.setVisibility(View.VISIBLE);
				} else {
					lo_tc4.setVisibility(View.GONE);
				}
			}
		});
		TextView tc5 = (TextView) findViewById(R.id.tc5);
		tc5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc5.getVisibility() == View.GONE) {
					lo_tc5.setVisibility(View.VISIBLE);
				} else {
					lo_tc5.setVisibility(View.GONE);
				}
			}
		});
		TextView tc6 = (TextView) findViewById(R.id.tc6);
		tc6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc6.getVisibility() == View.GONE) {
					lo_tc6.setVisibility(View.VISIBLE);
				} else {
					lo_tc6.setVisibility(View.GONE);
				}
			}
		});
		TextView tc7 = (TextView) findViewById(R.id.tc7);
		tc7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc7.getVisibility() == View.GONE) {
					lo_tc7.setVisibility(View.VISIBLE);
				} else {
					lo_tc7.setVisibility(View.GONE);
				}
			}
		});
		TextView tc8 = (TextView) findViewById(R.id.tc8);
		tc8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc8.getVisibility() == View.GONE) {
					lo_tc8.setVisibility(View.VISIBLE);
				} else {
					lo_tc8.setVisibility(View.GONE);
				}
			}
		});
		TextView tc9 = (TextView) findViewById(R.id.tc9);
		tc9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc9.getVisibility() == View.GONE) {
					lo_tc9.setVisibility(View.VISIBLE);
				} else {
					lo_tc9.setVisibility(View.GONE);
				}
			}
		});
		TextView tc10 = (TextView) findViewById(R.id.tc10);
		tc10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc10.getVisibility() == View.GONE) {
					lo_tc10.setVisibility(View.VISIBLE);
				} else {
					lo_tc10.setVisibility(View.GONE);
				}
			}
		});
		TextView tc11 = (TextView) findViewById(R.id.tc11);
		tc11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc11.getVisibility() == View.GONE) {
					lo_tc11.setVisibility(View.VISIBLE);
					scrollView.post(new Runnable() {
						@Override
						public void run() {
							scrollView.fullScroll(View.FOCUS_DOWN);
						}
					});
				} else {
					lo_tc11.setVisibility(View.GONE);
				}
			}
		});
		TextView tc12 = (TextView) findViewById(R.id.tc12);
		tc12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lo_tc12.getVisibility() == View.GONE) {
					lo_tc12.setVisibility(View.VISIBLE);
				} else {
					lo_tc12.setVisibility(View.GONE);
				}
			}
		});
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tc_delivery_detail.getLayoutParams();
		int short_distance_margin_padding = getResources().getDimensionPixelOffset(R.dimen.short_distance_margin_padding);
		int width = getScreenWidth() - short_distance_margin_padding * 2;
		int height = width * 427 / 898;
		params.width = width;
		params.height = height;
		tc_delivery_detail.setLayoutParams(params);
	}
	
	public int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

	@Override
	public void onClick(View arg0) {
	}
}
