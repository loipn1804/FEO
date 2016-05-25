package com.fareastorchid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.fareastorchid.listener.ProgressBarListener;
import com.fareastorchid.util.PQT;
import com.vng.android.slidingmenu.lib.SlidingMenu;
import com.vng.android.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.vng.android.slidingmenu.lib.app.SlidingFragmentActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends SlidingFragmentActivity
		implements
			ProgressBarListener {
	public ProgressBar mProgressbar;

	@Override
	public void showProgressBar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(ProgressBar.VISIBLE);
		}
	}

	@Override
	public void hideProgressBar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(ProgressBar.GONE);
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalligraphyConfig.initDefault("fonts/Gillsansmt.ttf", R.attr.fontPath);

		setBehindContentView(R.layout.view_sliding_menu_left);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				PQT.hideSoftKeyboard(BaseActivity.this);

			}
		});

		sm.setSecondaryMenu(getLayoutInflater().inflate(
				R.layout.view_sliding_menu_right, null));
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
