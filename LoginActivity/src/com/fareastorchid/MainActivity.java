package com.fareastorchid;

import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.HomeBannerAdapter;
import com.fareastorchid.adapter.HomeItemAdapter;
import com.fareastorchid.adapter.MainCatAdapter;
import com.fareastorchid.adapter.SubCateAdapter;
import com.fareastorchid.adapter.SubPriceListAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnFloristListener;
import com.fareastorchid.listener.OnOrderListener;
import com.fareastorchid.model.Banner;
import com.fareastorchid.model.CateType;
import com.fareastorchid.model.HomeItem;
import com.fareastorchid.model.MainCat;
import com.fareastorchid.model.PriceListSubCate;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Connectivity;
import com.fareastorchid.util.PQT;
import com.fareastorchid.view.LeftSlidingMenuView;
import com.fareastorchid.view.LeftSlidingMenuView.slidingMenuListener;
import com.fareastorchid.view.RightSlidingMenuView;
import com.nirhart.parallaxscroll.views.HeaderGridView;
import com.nirhart.parallaxscroll.views.ParallaxGridView;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends BaseActivity implements OnFloristListener,
		slidingMenuListener, OnOrderListener {

	public static String[] listSort = new String[] { "AZ", "ZA" };
	private final int PAGE_DEFAULT = 1;
	private int currentPage = PAGE_DEFAULT;
	private ImageView ivMenu;
	private ImageView ivSearch;
	private ImageView ivCart;
	private ImageView imvBackTop;
	private View viewGrayLayer;
	private ViewPager pager;
	private HomeBannerAdapter pagerAdapter;
	private ImageView ivCategory;
	private Dialog dialog, priceListDialog;
	private List<Banner> bannerList = new ArrayList<Banner>();
	private List<MainCat> catList = new ArrayList<MainCat>();
	private List<HomeItem> itemList = new ArrayList<HomeItem>();
	private List<CateType> typeList = new ArrayList<CateType>();
	private List<PriceListSubCate> priceList = new ArrayList<PriceListSubCate>();
	private ListView lvCatType, lvSortBy, lvSubCate, lvPriceList;
	private RelativeLayout panelSortby, panelFilterBy;
	private TextView tvSort, tvFilter;
	private MainCatAdapter cateTypeAdapter;
	private SortByAdapter sortByAdapter;
	private SubCateAdapter subCateAdapter;
	private SubPriceListAdapter priceListAdapter;
	private HeaderGridView lvMainItem;
	private CirclePageIndicator indicator;
	private SharedPreferences sharedpreferences;
	private HomeItemAdapter lvItemAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private boolean fistLoad = true;
	private boolean isNoMoreData = false;
	private boolean isLoading = false;
	private boolean isPull = false;
	private FloristBusiness doGetSubCate = new FloristBusinessImpl();
	private FloristBusiness doGetBanners = new FloristBusinessImpl();
	private FloristBusiness doGetCatType = new FloristBusinessImpl();
	private FloristBusiness doGetItemByCate = new FloristBusinessImpl();
	private FloristBusiness doGetItemByColorId = new FloristBusinessImpl();
	private FloristBusiness doGetItemByCountryId = new FloristBusinessImpl();
	private LeftSlidingMenuView leftSlidingMenuView;
	private RightSlidingMenuView rightSlidingMenuView;
	private FloristBusiness doGetAllCountry = new FloristBusinessImpl();
	private FloristBusiness doGetAllColor = new FloristBusinessImpl();
	private FloristBusiness getPriceListSub = new FloristBusinessImpl();
	private FloristBusiness getReport = new FloristBusinessImpl();
	private FloristBusiness getLastVersion = new FloristBusinessImpl();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Controller.updateActInstance(this);

		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES,
				Context.MODE_PRIVATE);

		GlobalData.shouldGetMainCat = true;
		GlobalData.shouldeGetItem = true;
		setContentView(R.layout.activity_main);

		initView();
		initBanner();
		setupSlidingMenu();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Log.w("onNewIntent", "onNewIntent");
			if (bundle.getBoolean("reloadItem", false)) {
				lvItemAdapter.notifyDataSetChanged();
			} else if (bundle.getBoolean("OpenRightMenu", false)) {
				showSecondaryMenu();
			}
		}
	}

	@SuppressLint("InflateParams")
	private void initView() {
		lvMainItem = (HeaderGridView) findViewById(R.id.lvMainItem);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View headerView = layoutInflater.inflate(
				R.layout.view_header_banner, null);

		// lvMainItem.setNumColumns(2);
		lvMainItem.addHeaderView(headerView);

		pager = (ViewPager) headerView.findViewById(R.id.home_viewpager);
		indicator = (CirclePageIndicator) headerView
				.findViewById(R.id.view_pager_indicator);
		LinearLayout lnlPager = (LinearLayout) headerView
				.findViewById(R.id.lnlPager);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lnlPager
				.getLayoutParams();
		params.height = getScreenWidth() * 145 / 480;
		lnlPager.setLayoutParams(params);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange_actionbar,
				R.color.orange_actionbar, R.color.orange_actionbar,
				R.color.orange_actionbar);

		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {

						isPull = true;
						currentPage = PAGE_DEFAULT;
						itemList.clear();
						lvItemAdapter.setListItem(itemList);
						lvItemAdapter.notifyDataSetChanged();
						if (GlobalData.currentMainCat == -1) {
							// country
							getItemByCountryId(GlobalData.subCate,
									GlobalData.sortBy, currentPage);
						} else if (GlobalData.currentMainCat == 0) {
							// color
							getItemByColorId(GlobalData.subCate,
									GlobalData.sortBy, currentPage);
						} else {
							getItemByMainCate(GlobalData.subCate,
									GlobalData.sortBy, currentPage);
						}
						// Handler handler = new Handler();
						// handler.postDelayed(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// // swipeRefreshLayout.setRefreshing(false);
						// getMainCat();
						// }
						// }, 3000);

					}
				});

		ivCategory = (ImageView) headerView.findViewById(R.id.ivCategory);
		mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
		cateTypeAdapter = new MainCatAdapter(this);
		subCateAdapter = new SubCateAdapter(this);
		lvItemAdapter = new HomeItemAdapter(this);
		sortByAdapter = new SortByAdapter(this);
		priceListAdapter = new SubPriceListAdapter(this);
		ivMenu = (ImageView) findViewById(R.id.main_menu);
		ivMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggle();
			}
		});

		ivSearch = (ImageView) findViewById(R.id.main_search);
		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						SearchActivity.class);
				MainActivity.this.startActivity(intent);

			}
		});
		imvBackTop = (ImageView) findViewById(R.id.main_imv_backtotop);
		imvBackTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (PQT.isNotNull(lvMainItem, lvMainItem.getAdapter())) {
					lvMainItem.smoothScrollToPosition(0);
				}
			}
		});

		ivCart = (ImageView) findViewById(R.id.main_cart);
		ivCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSecondaryMenu();
			}
		});
		dialog = new Dialog(this, R.style.DialogSlideAnim);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.setContentView(R.layout.view_hidden_panel);
		dialog.setOnShowListener(new Dialog.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				viewGrayLayer.setVisibility(View.VISIBLE);
			}
		});
		dialog.setOnDismissListener(new Dialog.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				viewGrayLayer.setVisibility(View.INVISIBLE);
				lvCatType.setVisibility(View.GONE);
				lvSubCate.setVisibility(View.GONE);
			}
		});

		ImageView ivBlur = (ImageView) dialog.findViewById(R.id.blur_iv);
		ivBlur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);

		lvSubCate = (ListView) dialog.findViewById(R.id.lv_subCate);
		lvCatType = (ListView) dialog.findViewById(R.id.lv_catType);
		lvSortBy = (ListView) dialog.findViewById(R.id.lv_sortBy);

		tvSort = (TextView) dialog.findViewById(R.id.tvSort);
		tvFilter = (TextView) dialog.findViewById(R.id.tvFilter);
		panelSortby = (RelativeLayout) dialog.findViewById(R.id.panel_sortby);
		panelSortby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lvSortBy.setVisibility(View.VISIBLE);
				lvCatType.setVisibility(View.GONE);
			}
		});
		panelFilterBy = (RelativeLayout) dialog
				.findViewById(R.id.panel_filterby);
		panelFilterBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lvCatType.setVisibility(View.VISIBLE);
				lvSortBy.setVisibility(View.GONE);
			}
		});

		ivCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				panelFilterBy.setVisibility(View.VISIBLE);
				dialog.show();
			}
		});

		viewGrayLayer = (View) findViewById(R.id.main_view_gray_layer);

		initPriceListDialog();
	}

	private void initPriceListDialog() {
		priceListDialog = new Dialog(this, R.style.DialogSlideAnim);
		priceListDialog.getWindow().setGravity(Gravity.BOTTOM);
		priceListDialog.setContentView(R.layout.view_hidden_pricelist);
		priceListDialog.setOnShowListener(new Dialog.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				viewGrayLayer.setVisibility(View.VISIBLE);
			}
		});
		priceListDialog.setOnDismissListener(new Dialog.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				viewGrayLayer.setVisibility(View.INVISIBLE);
			}
		});

		ImageView ivBlur = (ImageView) priceListDialog
				.findViewById(R.id.blur_iv);
		ivBlur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				priceListDialog.dismiss();
			}
		});
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = priceListDialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
		lvPriceList = (ListView) priceListDialog
				.findViewById(R.id.lv_pricelist);

	}

	private void initBanner() {
		pagerAdapter = new HomeBannerAdapter(getSupportFragmentManager());
		getBanners();
		pager.setAdapter(pagerAdapter);

		indicator.setViewPager(pager);
		indicator.setSnap(true);
		lvCatType.setAdapter(cateTypeAdapter);
		lvSortBy.setAdapter(sortByAdapter);
		lvSubCate.setAdapter(subCateAdapter);
		lvPriceList.setAdapter(priceListAdapter);
	}

	private void initPhoto() {
		if (GlobalData.shouldGetMainCat) {
			GlobalData.shouldGetMainCat = false;
			getMainCat();
		} else {
			cateTypeAdapter.setListCates(catList);
			cateTypeAdapter.notifyDataSetChanged();
		}
	}

	private void getSubCate(int mainCate) {
		doGetSubCate.setBusinessListener(new FloristBusinessListener() {

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
							JSONArray catJSONArray = root.getJSONArray("data");
							if (catJSONArray.length() != 0) {
								try {
									for (int i = 0; i < catJSONArray.length(); i++) {
										JSONObject data = catJSONArray
												.getJSONObject(i);
										CateType type = new CateType(data);
										typeList.add(type);
									}
									subCateAdapter.setListCates(typeList);
									subCateAdapter.notifyDataSetChanged();
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
		doGetSubCate.getByMainCat(mainCate);
	}

	private void getBanners() {
		doGetBanners.setBusinessListener(new FloristBusinessListener() {

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
			}

			@Override
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							JSONArray BannersJSONArray = root
									.getJSONArray("data");
							if (BannersJSONArray.length() != 0) {
								try {
									for (int i = 0; i < BannersJSONArray
											.length(); i++) {
										JSONObject data = BannersJSONArray
												.getJSONObject(i);
										Banner banner = new Banner(data);
										bannerList.add(banner);
									}
									pagerAdapter.setBannerList(bannerList);
									pagerAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		});
		doGetBanners.getAllBanner();
	}

	private void getMainCat() {
		doGetCatType.setBusinessListener(new FloristBusinessListener() {

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
							JSONArray catTypeJSONArray = root
									.getJSONArray("data");
							if (catTypeJSONArray.length() != 0) {
								try {
									// don't add cateId 0
									catList.add(new MainCat("-1", "Countries"));
									catList.add(new MainCat("0", "Colours"));

									for (int i = 0; i < catTypeJSONArray
											.length(); i++) {
										JSONObject data = catTypeJSONArray
												.getJSONObject(i);
										MainCat mainCat = new MainCat(data);
										catList.add(mainCat);
									}
									cateTypeAdapter.setListCates(catList);
									cateTypeAdapter.notifyDataSetChanged();
									leftSlidingMenuView.setListShopby(catList);
									// tvCategory.setText(catList.get(0).getCat_name());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							hideProgressBar();
							if (GlobalData.shouldeGetItem) {
								GlobalData.shouldeGetItem = false;
								if (PQT.isListDirty(catList))
									GlobalData.subCate = Integer
											.valueOf(catList.get(0).getId());

								currentPage = PAGE_DEFAULT;
								getItemByMainCate(GlobalData.subCate,
										GlobalData.sortBy, currentPage);
							} else {
								lvItemAdapter.setListItem(itemList);
								lvItemAdapter.notifyDataSetChanged();
							}
							lvMainItem.setAdapter(lvItemAdapter);
							lvMainItem
									.setOnScrollListener(new EndlessScrollListener());
						}
					}
				});
			}
		});
		doGetCatType.getAllMainCat();
	}

	private void getItemByMainCate(int cateTypeId, String sortBy, int pageIndex) {
		doGetItemByCate.setBusinessListener(new FloristBusinessListener() {

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

				isLoading = true;
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
							JSONArray itemJSONArray = root.getJSONArray("data");
							if (itemJSONArray.length() != 0) {
								isNoMoreData = false;
								try {
									for (int i = 0; i < itemJSONArray.length(); i++) {
										JSONObject data = itemJSONArray
												.getJSONObject(i);
										HomeItem item = new HomeItem(data);
										itemList.add(item);
									}
									lvItemAdapter.setListItem(itemList);
									lvItemAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								isNoMoreData = true;
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							if (isPull) {
								isPull = false;
								swipeRefreshLayout.setRefreshing(false);
							}
							fistLoad = false;
							currentPage++;
							hideProgressBar();
							isLoading = false;
						}
					}
				});
			}
		});
		doGetItemByCate.getItemFeoByMainCate(GlobalData.currentMainCat,
				cateTypeId, sortBy, pageIndex);
	}

	private void getItemByColorId(int colorId, String sortBy, int pageIndex) {
		doGetItemByColorId.setBusinessListener(new FloristBusinessListener() {

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
				isLoading = true;
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
							JSONArray itemJSONArray = root.getJSONArray("data");
							if (itemJSONArray.length() != 0) {
								isNoMoreData = false;
								try {
									for (int i = 0; i < itemJSONArray.length(); i++) {
										JSONObject data = itemJSONArray
												.getJSONObject(i);
										HomeItem item = new HomeItem(data);
										itemList.add(item);
									}
									lvItemAdapter.setListItem(itemList);
									lvItemAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								isNoMoreData = true;
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							fistLoad = false;
							currentPage++;
							hideProgressBar();
							isLoading = false;
						}
					}
				});
			}
		});
		doGetItemByCate.getItemFeoByColorId(colorId, sortBy, pageIndex);
	}

	private void getItemByCountryId(int colorId, String sortBy, int pageIndex) {
		doGetItemByCountryId.setBusinessListener(new FloristBusinessListener() {

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
				isLoading = true;
				showProgressBar();
			}

			@Override
			public void onDataProcessed(final Object entity) {
				Log.e("ThuanPQ", "onDataProcessed " + entity.toString());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							JSONArray itemJSONArray = root.getJSONArray("data");
							if (itemJSONArray.length() != 0) {
								isNoMoreData = false;
								try {
									for (int i = 0; i < itemJSONArray.length(); i++) {
										JSONObject data = itemJSONArray
												.getJSONObject(i);
										HomeItem item = new HomeItem(data);
										itemList.add(item);
									}
									lvItemAdapter.setListItem(itemList);
									lvItemAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								isNoMoreData = true;
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							fistLoad = false;
							currentPage++;
							hideProgressBar();
							isLoading = false;
						}
					}
				});
			}
		});

		doGetItemByCate.getItemFeoByCountryId(colorId, sortBy, pageIndex);
	}

	private void setupSlidingMenu() {
		// Left menu
		// *********************************************
		leftSlidingMenuView = LeftSlidingMenuView
				.getInstance(MainActivity.this);
		leftSlidingMenuView.setupMenu();
		// right menu
		// *********************************************
		rightSlidingMenuView = RightSlidingMenuView.getInstance(
				MainActivity.this, MainActivity.this, GlobalData.FROM_MAIN);
		rightSlidingMenuView.setupMenu();
	}

	@Override
	public void onClose() {
		super.onClose();
		viewGrayLayer.setVisibility(View.GONE);
	}

	@Override
	public void onOpen() {
		super.onOpen();
		viewGrayLayer.setVisibility(View.VISIBLE);

		rightSlidingMenuView.setupLvMenu();
	}

	@Override
	public void onChooseSortBy(String sortBy) {
		itemList.clear();
		isNoMoreData = false;
		lvItemAdapter.setListItem(itemList);
		lvItemAdapter.notifyDataSetChanged();
		GlobalData.sortBy = sortBy;

		currentPage = PAGE_DEFAULT;
		if (GlobalData.currentMainCat == -1) {
			// country
			getItemByCountryId(GlobalData.subCate, GlobalData.sortBy,
					currentPage);
		} else if (GlobalData.currentMainCat == 0) {
			// color
			getItemByColorId(GlobalData.subCate, GlobalData.sortBy, currentPage);
		} else {
			getItemByMainCate(GlobalData.subCate, GlobalData.sortBy,
					currentPage);
		}

		dialog.dismiss();
		lvSortBy.setVisibility(View.GONE);
		tvSort.setText(GlobalData.sortBy);
	}

	@Override
	public void onChooseCatType(MainCat cat) {
		showContent();
		if (dialog != null && !dialog.isShowing())
			dialog.show();

		viewGrayLayer.setVisibility(View.VISIBLE);

		GlobalData.currentMainCat = Integer.valueOf(cat.getId());

		lvCatType.setVisibility(View.GONE);

		lvSortBy.setVisibility(View.GONE);
		panelFilterBy.setVisibility(View.GONE);

		if (PQT.isListDirty(typeList))
			typeList.clear();

		tvFilter.setText(cat.getCat_name());

		if (cat.getId().equals("-1")) {
			// country
			lvSubCate.setVisibility(View.VISIBLE);
			getAllCountry();
		} else if (cat.getId().equals("0")) {
			// color
			lvSubCate.setVisibility(View.VISIBLE);
			getAllColor();
		} else {
			lvSubCate.setVisibility(View.VISIBLE);
			getSubCate(Integer.valueOf(cat.getId()));
		}
	}

	@Override
	public void onChooseSubCat(CateType catType) {
		itemList.clear();
		isNoMoreData = false;
		lvItemAdapter.setListItem(itemList);
		lvItemAdapter.notifyDataSetChanged();
		GlobalData.subCate = Integer.valueOf(catType.getId());
		currentPage = PAGE_DEFAULT;
		if (GlobalData.currentMainCat == -1) {
			// country
			getItemByCountryId(GlobalData.subCate, GlobalData.sortBy,
					currentPage);
		} else if (GlobalData.currentMainCat == 0) {
			// color
			getItemByColorId(GlobalData.subCate, GlobalData.sortBy, currentPage);
		} else {
			getItemByMainCate(GlobalData.subCate, GlobalData.sortBy,
					currentPage);
		}

		dialog.dismiss();
		lvSubCate.setVisibility(View.GONE);
		tvFilter.setText(catType.getName());
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupSlidingMenu();
		if (rightSlidingMenuView != null)
			rightSlidingMenuView.setupLvMenu();
		initPhoto();
		if (GlobalData.isFromPDFview) {
			GlobalData.isFromPDFview = false;
			showMenu();
		}

		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);

			int version = pInfo.versionCode;
			getLastVersion(version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	@Override
	public void onMenuClose() {
		showContent();
	}

	@Override
	public void onDeteleOrder() {
		rightSlidingMenuView.onDeteleOrder();
	}

	private void getAllCountry() {
		doGetAllCountry.setBusinessListener(new FloristBusinessListener() {

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
							JSONArray catTypeJSONArray = root
									.getJSONArray("data");
							if (catTypeJSONArray.length() != 0) {
								try {
									for (int i = 0; i < catTypeJSONArray
											.length(); i++) {
										JSONObject data = catTypeJSONArray
												.getJSONObject(i);
										CateType type = new CateType(data);
										typeList.add(type);
									}
									subCateAdapter.setListCates(typeList);
									subCateAdapter.notifyDataSetChanged();
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
		doGetAllCountry.getAllCountry();
	}

	private void getAllColor() {
		doGetAllColor.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						viewGrayLayer.setVisibility(View.GONE);
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
							JSONArray colorJSONArray = root
									.getJSONArray("data");
							if (colorJSONArray.length() != 0) {
								try {
									// don't add cateId 0
									for (int i = 1; i < colorJSONArray.length(); i++) {
										JSONObject data = colorJSONArray
												.getJSONObject(i);
										String name = (data
												.isNull("main_color") ? ""
												: data.getString("main_color"));
										String id = (data.isNull("id") ? ""
												: data.getString("id"));
										CateType type = new CateType(id, name);
										typeList.add(type);
									}
									typeList = sortByAlphabetical(typeList);
									subCateAdapter.setListCates(typeList);
									subCateAdapter.notifyDataSetChanged();

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

	private void doGetPriceListSub(String type) {
		getPriceListSub.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						viewGrayLayer.setVisibility(View.GONE);
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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							JSONArray subCateJSONArray = root
									.getJSONArray("data");
							if (subCateJSONArray.length() != 0) {
								try {
									for (int i = 0; i < subCateJSONArray
											.length(); i++) {
										JSONObject data = subCateJSONArray
												.getJSONObject(i);
										PriceListSubCate item = new PriceListSubCate(
												data);
										priceList.add(item);
									}
									priceListAdapter.setListCates(priceList);
									priceListAdapter.notifyDataSetChanged();
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
		getPriceListSub.getPriceListSubCate(type);
	}

	private void getLastVersion(final int version) {
		getLastVersion.setBusinessListener(new FloristBusinessListener() {

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
							// SimpleToast.info(MainActivity.this,
							// root.toString());
							int lastVersion = root.getInt("data");

							if (version < lastVersion) {
								new AlertDialog.Builder(MainActivity.this)
										.setTitle("Update Application")
										.setMessage(
												"You have to update new version to continue.")
										.setPositiveButton(
												"Update",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int which) {
														final String appPackageName = getPackageName(); // getPackageName()
																										// from
																										// Context
																										// or
																										// Activity
																										// object
														try {
															startActivity(new Intent(
																	Intent.ACTION_VIEW,
																	Uri.parse("market://details?id="
																			+ appPackageName)));
														} catch (android.content.ActivityNotFoundException anfe) {
															startActivity(new Intent(
																	Intent.ACTION_VIEW,
																	Uri.parse("http://play.google.com/store/apps/details?id="
																			+ appPackageName)));
														}
													}
												})
										.setNegativeButton(
												android.R.string.no,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int which) {
														GlobalData
																.clearGlobalData();
														finish();
													}
												})
										.setIcon(
												android.R.drawable.ic_dialog_alert)
										.show();
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
		getLastVersion.getLastVersion();
	}

	@Override
	public void onChoosePriceList(String type) {
		priceList.clear();
		priceListAdapter.notifyDataSetChanged();
		if (priceListDialog != null && !priceListDialog.isShowing())
			priceListDialog.show();

		lvPriceList.setVisibility(View.VISIBLE);
		viewGrayLayer.setVisibility(View.VISIBLE);

		GlobalData.currentPriceListName = type;

		if (type.equals("Other Countries") || type.equals("Malaysia Flowers")
				|| type.equals("Malaysia Foliage")) {
			doGetReport("-1", GlobalData.currentPriceListName, "");
		} else {
			doGetPriceListSub(type);
		}
	}

	@Override
	public void onChooseSubPriceList(PriceListSubCate subCate) {
		String id = subCate.getId();
		String name = GlobalData.currentPriceListName;
		Log.w("onChooseSubPriceList", id + "----"
				+ GlobalData.currentPriceListName);
		doGetReport(id, name, subCate.getname());
		lvPriceList.setVisibility(View.GONE);
	}

	private void doGetReport(String subCateId, final String priceListType,
			final String subPriceListType) {
		getReport.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onPreProcessed() {
				showProgressBar();
			}

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						viewGrayLayer.setVisibility(View.GONE);
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
			public void onDataProcessed(final Object entity) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							String data = root.getString("data");
							if (!data.equalsIgnoreCase("")) {
								try {
									String url = UrlHelper.FLORIST_URL + data;
									Log.w("---------------url--------------",
											url);
									viewGrayLayer.setVisibility(View.GONE);
									Intent i = new Intent(MainActivity.this,
											PDFViewerActivity.class);

									i.putExtra("url", url);
									i.putExtra("category", priceListType);
									i.putExtra("subCategory", subPriceListType);
									startActivity(i);
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
		getReport.getReportByCatId(subCateId, priceListType);
	}

	@Override
	public void onViewOrder() {
	}

	private static class SortByAdapter extends BaseAdapter {
		public LayoutInflater inflater;
		private OnFloristListener listener;

		public SortByAdapter(Context context) {
			super();
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.listener = (OnFloristListener) context;
		}

		@Override
		public int getCount() {
			if (listSort != null)
				return listSort.length;
			return 0;
		}

		@Override
		public String getItem(int position) {
			if (listSort != null)
				return listSort[position];
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
				if (listSort != null && position >= 0
						&& position < listSort.length) {
					holder.tvName.setText(listSort[position]);
					holder.tvName.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (listener != null) {
								listener.onChooseSortBy(listSort[position]);
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

	private class EndlessScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (imvBackTop.getVisibility() == View.VISIBLE
					&& firstVisibleItem == 0) {
				imvBackTop.setVisibility(View.INVISIBLE);
			} else if (imvBackTop.getVisibility() != View.VISIBLE
					&& firstVisibleItem != 0) {
				imvBackTop.setVisibility(View.VISIBLE);
			}

			if (isNoMoreData || fistLoad || isPull) {
				return;
			}

			int cols = getResources().getInteger(R.integer.num_grid_columns);
			int step = 0;
			if (cols == 3) {
				step = 0;
			} else {
				step = 2;
			}

			if (totalItemCount >= visibleItemCount + step
					&& !isLoading
					&& (totalItemCount - visibleItemCount + step) <= (firstVisibleItem
							+ step + 1)
					&& Connectivity.isConnected(MainActivity.this)) {
				// SimpleToast.info(MainActivity.this, "EndlessScrollListener "
				// + firstVisibleItem + " " + visibleItemCount + " " +
				// totalItemCount + " " + currentPage);
				Log.w("EndlessScrollListener", firstVisibleItem + ""
						+ visibleItemCount + "" + totalItemCount + currentPage);
				if (GlobalData.currentMainCat == -1) {
					// country
					getItemByCountryId(GlobalData.subCate, GlobalData.sortBy,
							currentPage);
				} else if (GlobalData.currentMainCat == 0) {
					// color
					getItemByColorId(GlobalData.subCate, GlobalData.sortBy,
							currentPage);
				} else {
					getItemByMainCate(GlobalData.subCate, GlobalData.sortBy,
							currentPage);
				}
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			return;
		}
	}

	private List<CateType> sortByAlphabetical(List<CateType> listDealAvailable) {

		Collections.sort(listDealAvailable, new Comparator<CateType>() {
			@Override
			public int compare(CateType object1, CateType object2) {
				return object1.getName().compareTo(object2.getName());
			}
		});

		return listDealAvailable;

	}

	public int getScreenWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		MainActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.widthPixels;
	}
}