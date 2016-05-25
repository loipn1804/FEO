package com.fareastorchid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.DiscountAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnFlowerItemListener;
import com.fareastorchid.listener.OnOrderListener;
import com.fareastorchid.model.DetailItem;
import com.fareastorchid.model.Discount;
import com.fareastorchid.model.FlowerDetailColor;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;
import com.fareastorchid.util.PQT;
import com.fareastorchid.util.StaticFunction;
import com.fareastorchid.view.LeftSlidingMenuView.slidingMenuListener;
import com.fareastorchid.view.RightSlidingMenuView;
import com.fareastorchid.view.SpecRequestDialog;
import com.fareastorchid.view.SpecRequestDialog.ConfirmButtonPressListener;

public class FlowerDetailActivity extends BaseActivity implements
		ConfirmButtonPressListener, slidingMenuListener, OnFlowerItemListener,
		OnOrderListener {

	private static List<String> names = new ArrayList<String>();
	private TextView tvChooseColor, tvCodeName;
	private EditText edtQuantity;
	private String itemId, item_status;
	private ImageView ivDetailImg;
	private TextView tvDetailName;
	private RelativeLayout rlAddCartCover;
	private TextView tvCountryName;
	private TextView tvDescription;
	private TextView tvPrice;
	private TextView tvTotalPrice;
	private TextView tvUpdateOn;
	private ColorAdapter colorAdapter;
	private ImageView addCart;
	private int quantity = 0;
	private SharedPreferences sharedpreferences;
	private SpecRequestDialog requestDialog;
	private ListView lvDiscount;
	private ImageView ivBack;
	private ImageView ivSearch;
	private ImageView ivCart;
	private View viewGrayLayer;
	private boolean isEditItem = false;
	private int editPosition = 0;
	private DetailItem detailItem;
	private Dialog dialog;
	private double sellingPrice;
	private double basePrice;
	private String address;
	private String specReq;
	private DiscountAdapter discountAdapter;
	private FloristBusiness doGetItemById = new FloristBusinessImpl();
	private RightSlidingMenuView rightSlidingMenuView;

	/**
	 * * Method for Setting the Height of the ListView dynamically. Hack to fix
	 * the issue of not showing all the items of the ListView when placed inside
	 * a ScrollView **
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flower_detail);
		Controller.updateActInstance(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES,
				Context.MODE_PRIVATE);
		Intent intent = getIntent();
		itemId = intent.hasExtra("itemId") ? intent.getStringExtra("itemId")
				: "-1";
		item_status = intent.hasExtra("item_status") ? intent
				.getStringExtra("item_status") : "";
		quantity = intent.getIntExtra("quantity", 0);
		editPosition = intent.getIntExtra("editPosition", 0);

		isEditItem = intent.getBooleanExtra("editItem", false);

		if (GlobalData.listColor != null) {
			names.clear();
			Collections.sort(GlobalData.listColor,
					new Comparator<FlowerDetailColor>() {
						@Override
						public int compare(FlowerDetailColor object1,
								FlowerDetailColor object2) {
							return object1.getName().compareTo(
									object2.getName());
						}
					});
			for (int i = 0; i < GlobalData.listColor.size(); i++) {
				Log.w("---------------names---------------",
						GlobalData.listColor.get(i).getName());
				names.add(GlobalData.listColor.get(i).getName());
			}
			// Collections.sort(names, new Comparator<String>() {
			// @Override
			// public int compare(String object1, String object2) {
			// return object1.compareTo(object2);
			// }
			// });
		} else {
			names.clear();
			names.add("none");
		}
		initView();
		// checkItemStatus();
		initPhoto();
		if (!itemId.equals("-1")) {
			getItemById(Integer.valueOf(itemId));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// registerReceiver(myBroadcastReceiver, new IntentFilter(""));
		setupSlidingMenu();
	}

	private void initView() {
		// initCommonView();
		mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
		tvCodeName = (TextView) findViewById(R.id.view_item_tv_codename);
		rlAddCartCover = (RelativeLayout) findViewById(R.id.detail_addtoCart_cover);
		tvChooseColor = (TextView) findViewById(R.id.detail_choose_color);
		tvChooseColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		addCart = (ImageView) findViewById(R.id.detail_addtoCart);
		addCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (detailItem == null)
					return;

				if (detailItem.getItem_status().equalsIgnoreCase("Sold Out")) {
					SimpleToast.info(
							Controller.getActInstance(MainActivity.class),
							"This item is out-of-stock, please try with another item");
					return;
				}

				String quantity = edtQuantity.getText().toString();

				if (quantity.equals("")) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Please change item quantity first");
					return;
				}

				int iQuantity = 0;
				try {
					iQuantity = Integer.parseInt(quantity);
				} catch (Exception ex) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Quantity must be a number");
					return;
				}

				if (iQuantity <= 0) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Item quantity can not be Zero, please change it or remove out of cart");
					return;
				}

				if (PQT.isNotNull(detailItem)) {
					onYesButtonPress("");
				}
			}
		});
		ivDetailImg = (ImageView) findViewById(R.id.flower_detail_img);
		tvDetailName = (TextView) findViewById(R.id.detail_flower_name);
		tvCountryName = (TextView) findViewById(R.id.detail_country_name);
		tvDescription = (TextView) findViewById(R.id.detail_description);
		tvPrice = (TextView) findViewById(R.id.flower_detail_tv_pricing);
		tvUpdateOn = (TextView) findViewById(R.id.detail_update_on);
		tvUpdateOn.setVisibility(View.GONE);

		tvTotalPrice = (TextView) findViewById(R.id.detail_total_price);
		edtQuantity = (EditText) findViewById(R.id.edtQuantity);

		ivDetailImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (detailItem != null) {
					if (detailItem.getImg_path() != null) {
						if (detailItem.getImg_path().length() > 0) {
							Intent intent = new Intent(
									FlowerDetailActivity.this,
									ImageActivity.class);
							intent.putExtra("imageUrl",
									detailItem.getImg_path());
							intent.putExtra("itemTitle",
									detailItem.getItem_name());
							startActivity(intent);
						}
					}
				}
			}
		});

		/*
		 * edtQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener()
		 * {
		 * 
		 * @Override public void onFocusChange(View v, boolean hasFocus) { if
		 * (hasFocus) {
		 * 
		 * edtQuantity.moveCursorToVisibleOffset();
		 * 
		 * 
		 * } }
		 * 
		 * });
		 */

		/*
		 * edtQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener()
		 * {
		 * 
		 * @Override public void onFocusChange(View v, boolean hasFocus) { if
		 * (hasFocus) { //Log.w("hasFocus", "hasFocus"); if
		 * (edtQuantity.getText().toString().equalsIgnoreCase("0")) {
		 * edtQuantity.setText(""); } else {
		 * edtQuantity.moveCursorToVisibleOffset(); }
		 * 
		 * } }
		 * 
		 * });
		 */

		edtQuantity.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// if (!s.toString().equalsIgnoreCase(""))

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateTotalPrice();
			}
		});

		lvDiscount = (ListView) findViewById(R.id.lvDiscount);

		if (GlobalData.isLogin) {
			lvDiscount.setVisibility(View.VISIBLE);
			discountAdapter = new DiscountAdapter(this);
			lvDiscount.setAdapter(discountAdapter);
		} else {
			lvDiscount.setVisibility(View.GONE);
		}

		ivBack = (ImageView) findViewById(R.id.detail_back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		ivSearch = (ImageView) findViewById(R.id.detail_search);
		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FlowerDetailActivity.this,
						SearchActivity.class);
				FlowerDetailActivity.this.startActivity(intent);

			}
		});

		ivCart = (ImageView) findViewById(R.id.detail_cart);
		ivCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSecondaryMenu();
			}
		});

		viewGrayLayer = (View) findViewById(R.id.detail_view_gray_layer);
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

	private void checkItemStatus() {
		if (!PQT.isStringDirty(item_status))
			return;

		if (item_status.equalsIgnoreCase("sold out")) {
			rlAddCartCover.setVisibility(View.GONE);
			findViewById(R.id.flower_detail_tbr_quantity).setVisibility(
					View.GONE);
			findViewById(R.id.flower_detail_v_devider).setVisibility(View.GONE);
			findViewById(R.id.view_item_tv_itemstatus).setVisibility(
					View.VISIBLE);
		}
	}

	private void updateTotalPrice() {
		try {
			if (PQT.isNotNull(detailItem)) {
				double totalPrice = 0;
				double price = 0;
				if (!edtQuantity.getText().toString().equalsIgnoreCase(""))
					quantity = Integer
							.valueOf(edtQuantity.getText().toString());
				else
					quantity = 0;

				if (GlobalData.isLogin) {
					price = Double.valueOf(detailItem.getTrader_price());
					if (sharedpreferences.contains(GlobalData.Email)) {
						address = sharedpreferences.getString(
								GlobalData.Office_No, "");
					}

				} else {
					price = Double.valueOf(detailItem.getWholesale_price());
					address = " ";
				}

				basePrice = price;
				if (detailItem.getLsDiscount() != null && GlobalData.isLogin) {
					int latestQty = 0;
					for (int i = 0; i < detailItem.getLsDiscount().size(); i++) {
						int curQty = Integer.valueOf(detailItem.getLsDiscount()
								.get(i).getTotal_item());
						if (quantity >= curQty && curQty > latestQty) {
							latestQty = curQty;
							price = basePrice
									- Double.valueOf(detailItem.getLsDiscount()
											.get(i).getAmount());
						}
					}
				}
				int quantity_per_uom = Integer.valueOf(detailItem
						.getQuantity_per_uom());
				if (quantity_per_uom == 0)
					quantity_per_uom = 1;

				totalPrice = price * quantity;
				sellingPrice = Double.valueOf(price);
				tvTotalPrice
						.setText("S$ " + Functions.round2String(totalPrice));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void initPhoto() {
		dialog = new Dialog(this, R.style.DialogSlideAnim);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.setContentView(R.layout.view_hidden_color);

		ImageView ivBlur = (ImageView) dialog.findViewById(R.id.blur_iv);
		ivBlur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		colorAdapter = new ColorAdapter(this);
		ListView lvColor = (ListView) dialog.findViewById(R.id.lv_color);
		lvColor.setAdapter(colorAdapter);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
	}

	private void getItemById(int itemId) {
		doGetItemById.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(
								""))
							SimpleToast.error(Controller
									.getActInstance(MainActivity.class),
									error_message.getError_message());
						else {
							SimpleToast.error(Controller
									.getActInstance(MainActivity.class),
									R.string.err_connect);
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
							JSONObject itemJSONObj = root.getJSONObject("data");
							if (PQT.isNotNull(itemJSONObj)) {
								DetailItem item = new DetailItem(itemJSONObj);
								if (PQT.isListDirty(GlobalData.listColor)) {
									item.setListColors(GlobalData.listColor);
								}
								setupItemDetail(item);
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
		doGetItemById.getItemFeoByItemId(itemId);
	}

	private void setupItemDetail(DetailItem _item) {
		if (_item == null)
			return;

		this.detailItem = _item;
		this.itemId = detailItem.getId();
		this.item_status = detailItem.getItem_status();

		tvChooseColor
				.setText((detailItem.getVarian_color().equals("none") ? detailItem
						.getMain_color() : detailItem.getVarian_color()));
		tvCodeName.setText("Code: " + detailItem.getCode_name());
		tvDetailName.setText(detailItem.getItem_name());
		tvDescription.setText(detailItem.getDescription());
		tvCountryName.setText(detailItem.getCountry_name());
		tvUpdateOn.setText("Update on: " + detailItem.getUpdateOn());
		if (GlobalData.isLogin) {
			tvPrice.setText("S$ " + detailItem.getTrader_price());
			GlobalData.itemPrice = Double.valueOf(detailItem.getTrader_price());
		} else {
			tvPrice.setText("S$ " + detailItem.getWholesale_price());
			GlobalData.itemPrice = Double.valueOf(detailItem
					.getWholesale_price());
		}

		if (isEditItem) {
			edtQuantity.setText(String.valueOf(quantity));
			edtQuantity.setSelection(edtQuantity.getText().length());
		} else {
			edtQuantity.setText("");
		}

		// setHeaderTitle(detailItem.getItem_name());
		// setHeaderLogo(R.drawable.feo_main_title);
		if (PQT.isStringDirty(detailItem.getImg_path())) {
			if (!detailItem.getImg_path().startsWith("/"))
				detailItem.setImg_path("/" + detailItem.getImg_path());
			String imgUrl = UrlHelper.FLORIST_IMG + detailItem.getImg_path();
			imgUrl = imgUrl.replace(" ", "%20");
			// UrlImageViewHelper.setUrlDrawable(ivDetailImg, imgUrl,
			// R.drawable.ic_loading);

			imgUrl = StaticFunction.checkLinkDetailActivity(imgUrl);

			StaticFunction.getImageLoader().displayImage(imgUrl, ivDetailImg,
					StaticFunction.getImageLoaderOptions());
		}

		if (GlobalData.isLogin) {
			discountAdapter.setListDiscounts(detailItem.getLsDiscount(),
					GlobalData.itemPrice);
			discountAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(lvDiscount);
		}
		checkItemStatus();
		updateTotalPrice();
	}

	@Override
	public void onBackPressed() {
		if (edtQuantity.getVisibility() == View.VISIBLE) {
			PQT.hideSoftKeyboard(FlowerDetailActivity.this);
		}
		super.onBackPressed();
	}

	@Override
	public void onYesButtonPress(String req) {
		if (requestDialog != null) {
			requestDialog.dismiss();
		}

		boolean isExist = false;
		int p = 0;
		if (PQT.isListDirty(GlobalData.listOrderItem)) {
			for (OrderItem item : GlobalData.listOrderItem) {
				p++;
				if (item.getId().equalsIgnoreCase(itemId)) {
					isExist = true;
					break;
				}
			}
		}
		editPosition = p - 1;
		if (isExist) { // isEditItem
			Log.w("OrderItem", itemId + "-" + quantity + "-" + sellingPrice
					+ "-" + basePrice + "-" + address + "-" + specReq + "-"
					+ "");
			OrderItem item = new OrderItem(detailItem, itemId, quantity,
					sellingPrice, basePrice, address, GlobalData.listOrderItem
							.get(editPosition).getSpecReq(),
					GlobalData.listOrderItem.get(editPosition).getTimeSlot());
			GlobalData.orderItem = item;
			GlobalData.listOrderItem.set(editPosition, item);

			SimpleToast.info(this, "Updated " + detailItem.getItem_name()
					+ "to cart");
		} else {
			specReq = req;
			Log.w("OrderItem", itemId + "-" + quantity + "-" + sellingPrice
					+ "-" + basePrice + "-" + address + "-" + specReq + "-"
					+ "");
			OrderItem item = new OrderItem(detailItem, itemId, quantity,
					sellingPrice, basePrice, address, specReq, "");
			GlobalData.orderItem = item;
			GlobalData.listOrderItem.add(item);

			SimpleToast.info(this,
					"Added " + quantity + " " + detailItem.getItem_name()
							+ "to cart");
			int cur_quantity = 0;
			if (GlobalData.isLogin && item.getItem().getLsDiscount() != null
					&& item.getItem().getLsDiscount().size() > 0) {
				for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
					OrderItem listItem = GlobalData.listOrderItem.get(i);
					if (listItem.getId().equalsIgnoreCase(itemId)) {
						cur_quantity += listItem.getQuantity();
					}
				}
				for (int j = item.getItem().getLsDiscount().size() - 1; j >= 0; j--) {
					Discount dis = item.getItem().getLsDiscount().get(j);
					if (cur_quantity >= Integer.valueOf(dis.getTotal_item())) {
						GlobalData.updateDiscountPrice(detailItem
								.getCode_name());
						break;
					}
				}
			}

		}
		setupSlidingMenu();
	}

	private void setupSlidingMenu() {
		// right menu
		// *********************************************
		rightSlidingMenuView = RightSlidingMenuView.getInstance(
				FlowerDetailActivity.this, FlowerDetailActivity.this,
				GlobalData.FROM_DETAIL);
		rightSlidingMenuView.setupMenu();
		rightSlidingMenuView.setupLvMenu();
	}

	@Override
	public void onMenuClose() {

	}

	@Override
	public void onClose() {
		super.onClose();
		viewGrayLayer.setVisibility(View.GONE);
	}

	@Override
	public void onOpen() {
		super.onOpen();
		Log.w("open", "open");
		viewGrayLayer.setVisibility(View.VISIBLE);

		rightSlidingMenuView.setupLvMenu();
	}

	@Override
	public void onItemColorListener(int pos) {
		if (PQT.isListDirty(GlobalData.listColor)) {
			FlowerDetailColor color = GlobalData.listColor.get(pos);
			if (color != null) {
				tvChooseColor.setText(color.getName());
				getItemById(Integer.valueOf(color.getId_item()));
			}
		}

		dialog.dismiss();
	}

	@Override
	public void onDeteleOrder() {
		rightSlidingMenuView.onDeteleOrder();
		rightSlidingMenuView.setupLvMenu();

	}

	@Override
	public void onViewOrder() {
		getSlidingMenu().showContent();
		OrderItem item = GlobalData.editOrderItem;
		itemId = item.getId();
		item_status = item.getItem().getItem_status();
		quantity = item.getQuantity();
		editPosition = GlobalData.editPosition;

		isEditItem = true;

		if (GlobalData.listColor != null) {
			names.clear();
			for (int i = 0; i < GlobalData.listColor.size(); i++) {
				names.add(GlobalData.listColor.get(i).getName());
			}
		} else {
			names.clear();
			names.add("none");
		}
		initView();
		// checkItemStatus();
		initPhoto();
		if (!itemId.equals("-1")) {
			getItemById(Integer.valueOf(itemId));
		}
	}

	private static class ColorAdapter extends BaseAdapter {
		public LayoutInflater inflater;
		private OnFlowerItemListener listener;

		public ColorAdapter(Context context) {
			super();
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.listener = (OnFlowerItemListener) context;
		}

		@Override
		public int getCount() {
			if (names != null)
				return names.size();
			return 0;
		}

		@Override
		public String getItem(int position) {
			if (names != null)
				return names.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			try {

				if (convertView == null) {
					holder = new ViewHolder();
					convertView = inflater.inflate(R.layout.view_panel_item,
							null);
					holder.tvName = (TextView) convertView
							.findViewById(R.id.tv_CatName);
					convertView.setTag(holder);

				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (names != null && position >= 0 && position < names.size()) {
					holder.tvName.setText(names.get(position));
					holder.tvName.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (listener != null) {
								listener.onItemColorListener(position);
							}
						}
					});

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		public static class ViewHolder {
			public TextView tvName;
		}
	}

}
