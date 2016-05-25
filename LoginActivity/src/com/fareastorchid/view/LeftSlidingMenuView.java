package com.fareastorchid.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fareastorchid.AboutUsActivity;
import com.fareastorchid.ContactUsActivity;
import com.fareastorchid.FAQActivity;
import com.fareastorchid.ForecastActivity;
import com.fareastorchid.LoginActivity;
import com.fareastorchid.MainActivity;
import com.fareastorchid.MyAccountActivity;
import com.fareastorchid.R;
import com.fareastorchid.TCActivity;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnFloristListener;
import com.fareastorchid.listener.ProgressBarListener;
import com.fareastorchid.model.MainCat;
import com.fareastorchid.model.controller.Controller;

public class LeftSlidingMenuView implements OnClickListener,
		ProgressBarListener {

	public ProgressBar mProgressbar;
	View ivClose;
	View tvMyAccount;
	View tvForecast;
	View tvPriceList;
	View tvShop;
//	View tvShopByCountry;
//	View tvShopByColor;
//	View tvShopByFreshFlower;
//	View tvShopByFoliage;
//	View tvShopByAccessory;
//	View tvShopByFestive;
	ImageButton btnLogin;
	View rlPricelistDetail;
	View rlShopDetail;
	// View tvPriceListNewShipment;
	View tvPriceListCountry;
	View tvPriceListAccessories;
	View tvPriceListFestive;
	View tvPriceListFixedIndia;
	View tvPriceListFixedMalaysiaFlo;
	View tvPriceListFixedMalaysiaFol;
	View tvPriceListFixed;
	View rlPriceListFixed;
	View tvContact;
	// View tvUserGuice;
	View tvFaq;
	View tvAbout;
	View tvTC;
	private LinearLayout lnlShopby;
	private Activity activity;
	private slidingMenuListener slListener;
	private OnFloristListener listener;

	private LeftSlidingMenuView(slidingMenuListener slListener) {
		super();
		this.activity = Controller.getActInstance(MainActivity.class);
		this.slListener = slListener;
		listener = (OnFloristListener) this.activity;

		ivClose = activity.findViewById(R.id.menu_close);
		tvMyAccount = activity.findViewById(R.id.menu_myacount);
		tvForecast = activity.findViewById(R.id.menu_forecast);
		rlPricelistDetail = activity.findViewById(R.id.menu_pricelist_detail);
		tvPriceList = activity.findViewById(R.id.menu_pricelist);
		// tvPriceListNewShipment =
		// activity.findViewById(R.id.pricelist_new_shipment);
		tvPriceListCountry = activity.findViewById(R.id.pricelist_country);
		tvPriceListAccessories = activity
				.findViewById(R.id.pricelist_accessories);
		tvPriceListFestive = activity.findViewById(R.id.pricelist_festive);
		tvPriceListFixed = activity.findViewById(R.id.pricelist_fixed);
		rlPriceListFixed = activity.findViewById(R.id.pricelist_fixed_layout);
		tvPriceListFixedIndia = activity
				.findViewById(R.id.pricelist_fixed_india);
		tvPriceListFixedMalaysiaFlo = activity
				.findViewById(R.id.pricelist_fixed_malay_flower);
		tvPriceListFixedMalaysiaFol = activity
				.findViewById(R.id.pricelist_fixed_malay_foliage);
		rlShopDetail = activity.findViewById(R.id.menu_shop_detail);
		tvShop = activity.findViewById(R.id.menu_shop);
//		tvShopByCountry = activity.findViewById(R.id.menu_shopby_country);
//		tvShopByColor = activity.findViewById(R.id.menu_shopby_colour);
//		tvShopByFreshFlower = activity
//				.findViewById(R.id.menu_shopby_freshflower);
//		tvShopByFoliage = activity.findViewById(R.id.menu_shopby_foliage);
//		tvShopByAccessory = activity.findViewById(R.id.menu_shopby_accessory);
//		tvShopByFestive = activity.findViewById(R.id.menu_shopby_festive);
//		tvShopByFreshFlower.setVisibility(View.GONE);
//		tvShopByFoliage.setVisibility(View.GONE);
//		tvShopByAccessory.setVisibility(View.GONE);
//		tvShopByFestive.setVisibility(View.GONE);
		lnlShopby = (LinearLayout) activity.findViewById(R.id.lnlShopby);

		tvFaq = activity.findViewById(R.id.menu_faq);
		tvTC = activity.findViewById(R.id.menu_tc);
		tvAbout = activity.findViewById(R.id.menu_aboutus);
		tvContact = activity.findViewById(R.id.menu_contactus);
		btnLogin = (ImageButton) activity.findViewById(R.id.menu_login);
		mProgressbar = (ProgressBar) activity.findViewById(R.id.progressbar);
	}

	public static LeftSlidingMenuView getInstance(slidingMenuListener slListener) {
		return new LeftSlidingMenuView(slListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_close:
			if (slListener != null)
				slListener.onMenuClose();
			break;

		case R.id.menu_myacount:
			if (GlobalData.isLogin) {
				Intent intentAccount = new Intent(activity,
						MyAccountActivity.class);
				activity.startActivity(intentAccount);
			} else {
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
			}
			break;

		case R.id.menu_forecast:
			Intent intent = new Intent(activity, ForecastActivity.class);
			activity.startActivity(intent);
			break;

		case R.id.menu_pricelist:
			if (rlPricelistDetail.getVisibility() == View.VISIBLE) {
				rlPricelistDetail.setVisibility(View.GONE);
			} else {
				rlPricelistDetail.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.menu_shop:
			if (rlShopDetail.getVisibility() == View.VISIBLE) {
				rlShopDetail.setVisibility(View.GONE);
			} else {
				rlShopDetail.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.menu_shopby_country:
			Intent intentCountry = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("-1", "Countries"));
			activity.startActivity(intentCountry);

			break;

		case R.id.menu_shopby_colour:
			Intent intentColours = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("0", "Colours"));
			activity.startActivity(intentColours);
			break;

		case R.id.menu_shopby_freshflower:
			Intent intentFreshFlower = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("1", "Fresh Flowers"));
			activity.startActivity(intentFreshFlower);

			break;

		case R.id.menu_shopby_foliage:
			Intent intentFoliage = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("2", "Foliages & Plants"));
			activity.startActivity(intentFoliage);

			break;

		case R.id.menu_shopby_accessory:
			Intent intentAccess = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("3", "Accessories"));
			activity.startActivity(intentAccess);

			break;

		case R.id.menu_shopby_festive:
			Intent intentFestive = new Intent(activity, MainActivity.class);
			listener.onChooseCatType(new MainCat("4", "Festive"));
			activity.startActivity(intentFestive);

			break;

		case R.id.menu_aboutus:
			Intent intentAbout = new Intent(activity, AboutUsActivity.class);
			activity.startActivity(intentAbout);
			break;

		case R.id.menu_contactus:
			Intent intentContact = new Intent(activity, ContactUsActivity.class);
			activity.startActivity(intentContact);
			break;

		case R.id.menu_faq:
			Intent intentFaq = new Intent(activity, FAQActivity.class);
			activity.startActivity(intentFaq);
			break;

		case R.id.menu_tc:
			Intent intentTC = new Intent(activity, TCActivity.class);
			activity.startActivity(intentTC);
			break;
		case R.id.menu_login:
			if (!GlobalData.isLogin) {
				Intent intentLogin = new Intent(activity, LoginActivity.class);
				activity.startActivity(intentLogin);
			} else {
				// new
				// AlertDialog.Builder(activity).setTitle("Logout").setMessage("Do you want to logout?").setPositiveButton(android.R.string.yes,
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				// GlobalData.clearGlobalData();
				// activity.finish();
				// Intent intent = new Intent(activity, LoginActivity.class);
				// activity.startActivity(intent);
				// }
				// }).setNegativeButton(android.R.string.no, new
				// DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				// }
				// }).setIcon(android.R.drawable.ic_dialog_alert).show();
				MessageDialog("Logout", "Do you want to logout?");
			}
			break;
		// case R.id.pricelist_new_shipment:
		// // doGetReport(1);
		// Intent intentPriceShipment = new Intent(activity,
		// MainActivity.class);
		// listener.onChoosePriceList("New Shipment");
		// activity.startActivity(intentPriceShipment);
		// if (slListener != null)
		// slListener.onMenuClose();
		// break;
		case R.id.pricelist_country:
			// doGetReport(2);
			Intent intentPriceCountry = new Intent(activity, MainActivity.class);
			listener.onChoosePriceList("Country Shipment");
			activity.startActivity(intentPriceCountry);
			if (slListener != null)
				slListener.onMenuClose();
			break;
		case R.id.pricelist_accessories:
			// doGetReport(3);
			Intent intentPriceAcc = new Intent(activity, MainActivity.class);
			listener.onChoosePriceList("Accessories");
			activity.startActivity(intentPriceAcc);
			if (slListener != null)
				slListener.onMenuClose();
			break;
		case R.id.pricelist_festive:
			// doGetReport(3);
			Intent intentPriceFev = new Intent(activity, MainActivity.class);
			listener.onChoosePriceList("Festive");
			activity.startActivity(intentPriceFev);
			if (slListener != null)
				slListener.onMenuClose();
			break;
		case R.id.pricelist_fixed:
			if (rlPriceListFixed.getVisibility() == View.VISIBLE) {
				rlPriceListFixed.setVisibility(View.GONE);
			} else {
				rlPriceListFixed.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.pricelist_fixed_india:
			Intent intentPriceIFlowers = new Intent(activity,
					MainActivity.class);
			listener.onChoosePriceList("Other Countries");
			activity.startActivity(intentPriceIFlowers);
			if (slListener != null)
				slListener.onMenuClose();
			break;
		case R.id.pricelist_fixed_malay_flower:
			Intent intentPriceMFlowers = new Intent(activity,
					MainActivity.class);
			listener.onChoosePriceList("Malaysia Flowers");
			activity.startActivity(intentPriceMFlowers);
			if (slListener != null)
				slListener.onMenuClose();
			break;

		case R.id.pricelist_fixed_malay_foliage:
			Intent intentPriceMFoliage = new Intent(activity,
					MainActivity.class);
			listener.onChoosePriceList("Malaysia Foliage");
			activity.startActivity(intentPriceMFoliage);
			if (slListener != null)
				slListener.onMenuClose();
			break;
		default:
			break;
		}
	}

	public void setupMenu() {
		ivClose.setOnClickListener(this);
		tvMyAccount.setOnClickListener(this);
		tvForecast.setOnClickListener(this);
		tvPriceList.setOnClickListener(this);
		tvShop.setOnClickListener(this);
//		tvShopByCountry.setOnClickListener(this);
//		tvShopByColor.setOnClickListener(this);
//		tvShopByFreshFlower.setOnClickListener(this);
//		tvShopByFoliage.setOnClickListener(this);
//		tvShopByAccessory.setOnClickListener(this);
//		tvShopByFestive.setOnClickListener(this);
		tvFaq.setOnClickListener(this);
		tvTC.setOnClickListener(this);
		tvAbout.setOnClickListener(this);
		tvContact.setOnClickListener(this);
		// tvUserGuice.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		// tvPriceListNewShipment.setOnClickListener(this);
		tvPriceListCountry.setOnClickListener(this);
		tvPriceListAccessories.setOnClickListener(this);
		tvPriceListFestive.setOnClickListener(this);
		tvPriceListFixed.setOnClickListener(this);

		tvPriceListFixedIndia.setOnClickListener(this);
		tvPriceListFixedMalaysiaFlo.setOnClickListener(this);
		tvPriceListFixedMalaysiaFol.setOnClickListener(this);
		if (!GlobalData.isLogin) {
			tvPriceList.setVisibility(View.GONE);
			btnLogin.setBackgroundResource(R.drawable.feo_ios_log_in_login);
		} else {
			tvPriceList.setVisibility(View.VISIBLE);
			btnLogin.setBackgroundResource(R.drawable.profile_logout);
		}
	}

	public void setListShopby(List<MainCat> catList) {
		lnlShopby.removeAllViews();
		for (final MainCat mainCat : catList) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TextView txtName = (TextView) inflater.inflate(
					R.layout.view_shop_category_item, null);
			txtName.setText(mainCat.getCat_name());
			txtName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intentCountry = new Intent(activity,
							MainActivity.class);
					listener.onChooseCatType(new MainCat(mainCat.getId(),
							mainCat.getCat_name()));
					activity.startActivity(intentCountry);
				}
			});
			lnlShopby.addView(txtName);
		}
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

	public interface slidingMenuListener {
		public void onMenuClose();
	}

	public void MessageDialog(String title, String message) {

		final Dialog dialog = new Dialog(activity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.view_message_confirm);

		((TextView) dialog.findViewById(R.id.popup_tv_title)).setText(title);
		((TextView) dialog.findViewById(R.id.popup_tv_message))
				.setText(message);
		ImageButton imbYes = (ImageButton) dialog
				.findViewById(R.id.popup_imb_yes);
		ImageButton imbNo = (ImageButton) dialog
				.findViewById(R.id.popup_imb_no);

		imbNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		imbYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				GlobalData.clearGlobalData();
				activity.finish();
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
			}
		});

		dialog.show();
	}
}
