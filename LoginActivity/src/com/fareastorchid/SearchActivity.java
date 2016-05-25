package com.fareastorchid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.adapter.SearchItemAdapter;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.HomeItem;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Connectivity;
import com.fareastorchid.util.PQT;

public class SearchActivity extends AbstractActivity {

	private int totalResult = 0;
	private boolean isLoading = false;
	private EditText edtSearch;
	private TextView tvTotal;
	private GridView gvSearch;
	private List<HomeItem> lsSearchItems = new ArrayList<HomeItem>();
	private TextWatcher watch = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence s, int a, int b, int c) {
			if (s.toString().equals("@api")) {
				SimpleToast.info(SearchActivity.this,
						UrlHelper.FLORIST_API_DOMAIN);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			lsSearchItems.clear();
			searchAdapter.setListItem(lsSearchItems);
			searchAdapter.notifyDataSetChanged();

			resetPageIndex();
			totalResult = 0;
			tvTotal.setText(totalResult + " results found");
			if (s.length() >= 1)
				getItemByMainCate(s.toString());
		}
	};
	private SearchItemAdapter searchAdapter;
	private Dialog dialog;
	private View viewGrayLayer;
	private TextView tvName;
	private TextView tvCode;
	private TextView tvType;
	private FloristBusiness doSearchItemByName = FloristBusinessImpl
			.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Controller.updateActInstance(this);
		initView();
	}

	private void initView() {
		initCommonView();
		setHeaderTitle("Search");

		edtSearch = (EditText) findViewById(R.id.edtSearch);
		edtSearch.addTextChangedListener(watch);

		tvType = (TextView) findViewById(R.id.tvType);
		tvTotal = (TextView) findViewById(R.id.searchTotal);
		searchAdapter = new SearchItemAdapter(this);
		gvSearch = (GridView) findViewById(R.id.gvSearch);
		gvSearch.setAdapter(searchAdapter);
		gvSearch.setOnScrollListener(new EndlessScrollListener());

		dialog = new Dialog(this, R.style.DialogSlideAnim);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.setContentView(R.layout.view_panel_search);
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
		viewGrayLayer = (View) findViewById(R.id.search_grey);
		viewGrayLayer.setVisibility(View.GONE);
		tvName = (TextView) dialog.findViewById(R.id.search_name);
		tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				tvType.setText("Name");
				GlobalData.searchType = "item_name";
			}
		});

		tvCode = (TextView) dialog.findViewById(R.id.search_code);
		tvCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				tvType.setText("Code");
				GlobalData.searchType = "code_name";
			}
		});
		tvType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// dialog.show();
			}
		});
		tvType.setVisibility(View.GONE);
	}

	private void getItemByMainCate(String itemName) {
		if (PQT.isNotNull(doSearchItemByName))
			doSearchItemByName.removeListener();
		doSearchItemByName = FloristBusinessImpl.getInstance();

		doSearchItemByName.setBusinessListener(new FloristBusinessListener() {

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
				isLoading = true;
			}

			@Override
			public void onDataProcessed(final Object entity) {
				isLoading = false;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject object = (JSONObject) entity;
							JSONObject root = object.getJSONObject("root");
							totalResult = root.getInt("totalResult");

							if (totalResult == 1) {
								tvTotal.setText(totalResult + " result found");
							} else {
								tvTotal.setText(totalResult + " results found");
							}

							JSONArray itemJSONArray = root.getJSONArray("data");
							if (itemJSONArray.length() != 0) {
								try {
									for (int i = 0; i < itemJSONArray.length(); i++) {
										JSONObject data = itemJSONArray
												.getJSONObject(i);
										HomeItem item = new HomeItem(data);

										lsSearchItems.add(item);
									}
									searchAdapter.setListItem(lsSearchItems);
									searchAdapter.notifyDataSetChanged();
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
		// doSearchItemByName.searchItemByName(GlobalData.searchType, itemName,
		// pageIndex);
		doSearchItemByName.searchItemByName("", itemName, pageIndex);
	}

	private class EndlessScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (totalItemCount < totalResult
					&& totalItemCount >= visibleItemCount
					&& !isLoading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + 3)
					&& Connectivity.isConnected(SearchActivity.this)) {
				increasePageIndex();
				getItemByMainCate(edtSearch.getText().toString());
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			return;
		}
	}
}