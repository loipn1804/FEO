package com.fareastorchid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.Day;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.wheel.AbstractWheel;
import com.fareastorchid.wheel.OnWheelScrollListener;
import com.fareastorchid.wheel.adapter.AbstractWheelTextAdapter;

public class DeliveryTimeActivity extends AbstractActivity {

	private final String[] fullSlotTimes = new String[] { "9 - 12 pm", "12 - 5 pm" };
	private final String[] secondSlotTime = new String[] { "12 - 5 pm" };
	private final String[] notDeliveryDay = new String[] { "1-1", "19-2", "20-2", "3-4", "1-5", "1-6", "17-7", "9-8", "24-9", "10-11", "25-12" };
	String[] times = null;
	boolean setMargin4Divider = false;
	private List<Day> listDays = null;
	private ImageView ivConfirm;
	private DayAdapter dayAdapter;
	private AbstractWheel wheelDay, wheelTime;
	private String chosenDate = "";
	private String chosenTime = "";
	private boolean isOutFirstTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalligraphyConfig.initDefault("fonts/Gillsansmt.ttf", R.attr.fontPath);
		setContentView(R.layout.activity_delivery_time);

		Controller.updateActInstance(this);
		initChild();
		initView();
	}

	private void initChild() {
		listDays = new ArrayList<Day>();
		times = fullSlotTimes;
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	private void initView() {
		initCommonView();
		setHeaderTitle("Delivery Time");

		isOutFirstTime = isOutFirstSlot();

		if (isOutFirstTime) {
			times = secondSlotTime;
		} else {
			times = fullSlotTimes;
		}

		chosenTime = times[0];

		wheelTime = (AbstractWheel) findViewById(R.id.time);
		wheelDay = (AbstractWheel) findViewById(R.id.day);

		wheelTime.setVisibleItems(2);
		wheelTime.setViewAdapter(new TimeAdapter(DeliveryTimeActivity.this));

		wheelTime.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				chosenTime = times[wheel.getCurrentItem()];
			}
		});

		wheelTime.setCurrentItem(3);
		wheelDay.setVisibleItems(5);
		dayAdapter = new DayAdapter(this);
		getListDates();
		wheelDay.setViewAdapter(dayAdapter);
		chosenDate = listDays.get(0).getWeek() + " " + listDays.get(0).getDay_no() + " " + listDays.get(0).getMonth();

		wheelDay.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				int range = wheel.getCurrentItem();

				if (range == 0 && isOutFirstTime) {
					times = secondSlotTime;
				} else {
					times = fullSlotTimes;
				}
				chosenTime = times[0];
				Day cur_day = listDays.get(range);
				chosenDate = cur_day.getWeek() + " " + cur_day.getDay_no() + " " + cur_day.getMonth();
				wheelTime.setViewAdapter(new TimeAdapter(DeliveryTimeActivity.this));
				wheelTime.setCurrentItem(0);
			}
		});

		ivConfirm = (ImageView) findViewById(R.id.delivery_time_confirm);
		ivConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GlobalData.chosenTimeSlot = chosenTime + " - " + chosenDate;
				finish();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (!setMargin4Divider) {
			RelativeLayout rlCover = (RelativeLayout) findViewById(R.id.delivery_time_rl_cover);

			View divider1 = (View) findViewById(R.id.delivery_time_view_divider1);
			View divider2 = (View) findViewById(R.id.delivery_time_view_divider2);

			int sampleHeight = getResources().getDimensionPixelSize(R.dimen.sample_height);
			int firstMargin = (rlCover.getHeight() - sampleHeight) / 2;
			int secondMargin = (rlCover.getHeight() + sampleHeight) / 2;

			MarginLayoutParams marginParams = new MarginLayoutParams(divider1.getLayoutParams());
			marginParams.setMargins(0, firstMargin, 0, 0);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
			divider1.setLayoutParams(layoutParams);

			MarginLayoutParams marginParams2 = new MarginLayoutParams(divider2.getLayoutParams());
			marginParams2.setMargins(0, secondMargin, 0, 0);
			RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(marginParams2);
			divider2.setLayoutParams(layoutParams2);
		}
	}

	private void getListDates() {
		String dayName = "";
		String monthName = "";
		Calendar c = Calendar.getInstance();
		boolean isOutCurrentDay = isOutCurrentDay();
		int i = 0;
		do {
			if (i == 0 && !isOutCurrentDay && !isInNotDeliveryDay(c)) {
				dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
				monthName = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
				Day day = new Day(String.valueOf(c.get(Calendar.DATE)), monthName, dayName, false);
				listDays.add(day);
			} else {
				c.add(Calendar.DATE, 1);
				if (!isInNotDeliveryDay(c)) {
					dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
					monthName = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
					Day day = new Day(String.valueOf(c.get(Calendar.DATE)), monthName, dayName, false);
					listDays.add(day);
				}
			}

			i++;

		} while (i < 30);

		dayAdapter.setListDays(listDays);
	}

	private boolean isOutFirstSlot() {
		boolean isOutTime = false;
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour > 7 && hour < 12) {
			isOutTime = true;
		}

		return isOutTime;
	}

	private boolean isOutCurrentDay() {
		boolean isOutDay = false;
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (hour >= 12) {
			isOutDay = true;
		}

		return isOutDay;
	}

	private boolean isInNotDeliveryDay(Calendar c) {
		int month = c.get(Calendar.MONTH) + 1;
		String day = c.get(Calendar.DATE) + "-" + month;
		boolean isSunday = (c.get(Calendar.DAY_OF_WEEK) == 1);
		for (int i = 0; i < notDeliveryDay.length; i++) {
			if (day.equalsIgnoreCase(notDeliveryDay[i]) || isSunday) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adapter for Time
	 */
	private class TimeAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		protected TimeAdapter(Context context) {
			super(context, R.layout.delivery_item_time, NO_RESOURCE);
			setItemTextResource(R.id.tvTimes);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return times.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return times[index];
		}
	}

	private class DayAdapter extends AbstractWheelTextAdapter {
		private List<Day> listDays;

		/**
		 * Constructor
		 */
		protected DayAdapter(Context context) {
			super(context, R.layout.delivery_item_date, NO_RESOURCE);

			setItemTextResource(R.id.tvTimes);
		}

		public void setListDays(List<Day> listDays) {
			this.listDays = listDays;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			view.findViewById(R.id.item_date);
			TextView tvDate = (TextView) view.findViewById(R.id.del_date_day);
			TextView tvMonth = (TextView) view.findViewById(R.id.del_date_month);
			TextView tvWeek = (TextView) view.findViewById(R.id.del_week);
			if (listDays != null && index >= 0 && index < listDays.size()) {
				final Day day = listDays.get(index);
				if (day != null) {
					if (!day.getDay_no().isEmpty()) {
						tvDate.setText(day.getDay_no());
					}
					if (!day.getMonth().isEmpty()) {
						tvMonth.setText(day.getMonth());
					}
					if (!day.getWeek().isEmpty()) {
						tvWeek.setText(day.getWeek());
					}
				}
			}
			return view;
		}

		@Override
		public int getItemsCount() {
			return listDays.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return listDays.get(index).getDay_no();
		}
	}
}
