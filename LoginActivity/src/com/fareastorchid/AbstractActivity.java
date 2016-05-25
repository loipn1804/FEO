package com.fareastorchid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fareastorchid.listener.ProgressBarListener;

public class AbstractActivity extends Activity implements ProgressBarListener {

	protected final int PAGE_DEFAULT = 1;
	protected int pageIndex = PAGE_DEFAULT;
	private ProgressBar mProgressbar;
	private ImageView headerBack;
	private TextView headerTitle;
	private ImageView headerLogo;

	protected void increasePageIndex() {
		pageIndex++;
	}

	protected void decreasePageIndex() {
		if (pageIndex > 1)
			pageIndex--;
	}

	protected void resetPageIndex() {
		pageIndex = PAGE_DEFAULT;
	}

	protected void setHeaderLogo(int resId) {
		if (headerTitle != null)
			headerTitle.setVisibility(View.GONE);
		headerLogo.setVisibility(View.VISIBLE);

		headerLogo.setImageResource(resId);
	}

	protected void setHeaderTitle(String title) {
		if (headerLogo != null)
			headerLogo.setVisibility(View.GONE);
		headerTitle.setVisibility(View.VISIBLE);

		headerTitle.setText(title);
	}

	protected void setHeaderTitle(int resId) {
		if (headerLogo != null)
			headerLogo.setVisibility(View.GONE);
		headerTitle.setVisibility(View.VISIBLE);

		headerTitle.setText(getString(resId));
	}

	protected void initCommonView() {
		mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
		headerTitle = (TextView) findViewById(R.id.header_title);
		headerBack = (ImageView) findViewById(R.id.header_back);
		headerLogo = (ImageView) findViewById(R.id.header_logo);

		if (headerBack != null) {
			headerBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
		CalligraphyConfig.initDefault("fonts/Gillsansmt.ttf", R.attr.fontPath);
	}

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

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
