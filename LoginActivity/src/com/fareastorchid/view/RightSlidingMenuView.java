package com.fareastorchid.view;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.MainActivity;
import com.fareastorchid.R;
import com.fareastorchid.ReserveActivity;
import com.fareastorchid.adapter.RightMenuOrderAdapter;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;
import com.fareastorchid.view.LeftSlidingMenuView.slidingMenuListener;

public class RightSlidingMenuView implements OnClickListener {

	View btnOrder;
	View ivRightClose;
	ListView lvRightMenu;
	TextView menu_total, tvNoItem, tvCartCount;
	private slidingMenuListener slListener;
	private RightMenuOrderAdapter adapter;
	private ImageView ivTrash;
	private double total = 0;
	private boolean toggle = false;

	public RightSlidingMenuView(slidingMenuListener slListener, Activity activity, int from) {
		super();
		this.slListener = slListener;

		btnOrder = activity.findViewById(R.id.menu_order);
		ivRightClose = activity.findViewById(R.id.menu_right_close);
		lvRightMenu = (ListView) activity.findViewById(R.id.lvRightMenu);
		menu_total = (TextView) activity.findViewById(R.id.menu_total);
		tvNoItem = (TextView) activity.findViewById(R.id.menu_no_item_in_cart);
		tvCartCount = (TextView) activity.findViewById(R.id.main_tv_cart_count);
		ivTrash = (ImageView) activity.findViewById(R.id.menu_right_trash);
		adapter = new RightMenuOrderAdapter(activity);
		adapter.setOpenFrom(from);
	}

	public static RightSlidingMenuView getInstance(slidingMenuListener slListener, Activity activity, int from) {
		return new RightSlidingMenuView(slListener, activity, from);
	}

	public void setupMenu() {
		btnOrder.setOnClickListener(this);
		ivRightClose.setOnClickListener(this);
	}

	public void setupLvMenu() {
		if (!PQT.isListDirty(GlobalData.listOrderItem)) {
			tvNoItem.setVisibility(View.VISIBLE);
			tvCartCount.setVisibility(View.GONE);
		} else {
			tvNoItem.setVisibility(View.GONE);
			tvCartCount.setVisibility(View.VISIBLE);
			tvCartCount.setText(String.valueOf(GlobalData.listOrderItem.size()));
		}

		Log.w("setupLvMenu", GlobalData.listOrderItem.size() + "");
		total = 0;
		adapter.setListItem(GlobalData.listOrderItem);
		adapter.notifyDataSetChanged();
		lvRightMenu.setAdapter(adapter);
		for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
			total += GlobalData.listOrderItem.get(i).getSellingPrice() * GlobalData.listOrderItem.get(i).getQuantity();
		}
		menu_total.setText("S$ " + Functions.round2String(total));
		ivTrash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle = !toggle;
				adapter.isShowDelete(toggle);
				adapter.notifyDataSetChanged();

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_order:
			if (!PQT.isListDirty(GlobalData.listOrderItem)) {
				SimpleToast.error(Controller.getActInstance(MainActivity.class), "Your cart is empty");
			} else {
				Intent intent = new Intent(Controller.getActInstance(MainActivity.class), ReserveActivity.class);
				Controller.getActInstance(MainActivity.class).startActivity(intent);
			}
			break;

		case R.id.menu_right_close:
			slListener.onMenuClose();
			break;

		default:
			break;
		}
	}

	public void onDeteleOrder() {
		total = 0;
		for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
			total += GlobalData.listOrderItem.get(i).getSellingPrice();
		}
		menu_total.setText("S$ " + total);
	}
}
