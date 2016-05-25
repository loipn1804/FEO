package com.fareastorchid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.view.UpdatePasswordDialog;
import com.fareastorchid.view.UpdatePasswordDialog.ChangePwdButtonPressListener;

public class MyAccountActivity extends AbstractActivity implements OnClickListener, ChangePwdButtonPressListener {

	private ImageView ivOrderHistory, ivOutlet;
	private EditText edtName, edtEmail, edtContactNumber, edtShopName, edtShopAddress;
	private TextView tvLevel, tvEdit, tvPosition;
	private ImageView ivChangePw, ivLogOut, ivEdit;
	private SharedPreferences sharedpreferences;
	private UpdatePasswordDialog updatePasswordDialog;
	private FloristBusiness floristBusiness = new FloristBusinessImpl();
	private boolean onEditMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_account);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		Controller.updateActInstance(this);
		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES, Context.MODE_PRIVATE);
		initView();
	}

	private void initView() {
		initCommonView();
		setHeaderLogo(R.drawable.feo_ios_account_page_account);

		// edtBillingAddress = (EditText)
		// findViewById(R.id.account_billing_address);
		edtName = (EditText) findViewById(R.id.account_fullname);
		edtEmail = (EditText) findViewById(R.id.account_email);
		edtContactNumber = (EditText) findViewById(R.id.account_contact_number);
		edtShopName = (EditText) findViewById(R.id.account_shop_name);
		edtShopAddress = (EditText) findViewById(R.id.account_shop_address);
		ivChangePw = (ImageView) findViewById(R.id.account_changepw);
		ivLogOut = (ImageView) findViewById(R.id.account_logout);
		ivOrderHistory = (ImageView) findViewById(R.id.account_orderhis);
		ivOutlet = (ImageView) findViewById(R.id.account_outlet);
		ivEdit = (ImageView) findViewById(R.id.account_address_edit);
		tvLevel = (TextView) findViewById(R.id.account_level);
		tvPosition = (TextView) findViewById(R.id.account_position);
		tvEdit = (TextView) findViewById(R.id.account_edit);

		edtName.setText(sharedpreferences.getString(GlobalData.FullName, ""));
		edtEmail.setText(sharedpreferences.getString(GlobalData.Email, ""));
		edtContactNumber.setText(sharedpreferences.getString(GlobalData.ContactNumber, ""));
		edtShopName.setText(sharedpreferences.getString(GlobalData.ShopName, ""));
		edtShopAddress.setText(sharedpreferences.getString(GlobalData.BillingAddress, ""));
		// edtBillingAddress.setText(sharedpreferences.getString(GlobalData.BillingAddress,
		// ""));
		// tvLevel.setText(sharedpreferences.getString(GlobalData.Level, ""));
		tvLevel.setText("Trader");
		tvPosition.setText(sharedpreferences.getString(GlobalData.Position, "N/A"));

		ivChangePw.setOnClickListener(this);
		ivLogOut.setOnClickListener(this);
		ivOrderHistory.setOnClickListener(this);
		ivOutlet.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
//		ivEdit.setOnClickListener(this);
		ivEdit.setVisibility(View.GONE);
		tvEdit.setVisibility(View.GONE);
	}

	private void updateProfile(String id_user, final String fullname, final String email, final String contactNumber, final String shopName, final String shopAddress) {
		floristBusiness.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(ErrorMessage error_message) {
				Log.w("error", "error");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
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
							String status = root.getString("status");
							if (status.equals("success")) {
								Editor editor = sharedpreferences.edit();
								editor.putString(GlobalData.FullName, fullname);
								editor.putString(GlobalData.Email, email);
								editor.putString(GlobalData.ShopName, shopName);
								// editor.putString(GlobalData.BillingAddress,
								// billAddress);
								editor.putString(GlobalData.Office_No, shopAddress);
								editor.putString(GlobalData.ContactNumber, contactNumber);
								editor.commit();
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
		floristBusiness.userFeoUpdateProfile(id_user, fullname, email, contactNumber, shopAddress, shopName);
	}

	private void updatePwd(String username, String curPwd, String newPwd) {
		floristBusiness.setBusinessListener(new FloristBusinessListener() {
			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						hide_keyboard_from(MyAccountActivity.this, findViewById(R.id.root));
						if (!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(MainActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(MainActivity.class), R.string.err_connect);
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
							String status = root.getString("status");
							if (!status.equals("success")) {
								SimpleToast.error(Controller.getActInstance(MainActivity.class), root.getString("message"));
							} else {
								SimpleToast.ok(Controller.getActInstance(MainActivity.class), root.getString("message"));
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							hideProgressBar();
							hide_keyboard_from(MyAccountActivity.this, findViewById(R.id.root));
						}
					}

				});
			}
		});

		floristBusiness.userFeoChangePW(username, curPwd, newPwd);
	}

	@Override
	public void onChangePwdButtonPress(String curPwd, String newPwd) {
		if (updatePasswordDialog != null && updatePasswordDialog.isShowing())
			updatePasswordDialog.dismiss();

		updatePwd(sharedpreferences.getString(GlobalData.UserName, ""), curPwd, newPwd);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_changepw:
			updatePasswordDialog = new UpdatePasswordDialog(MyAccountActivity.this, MyAccountActivity.this);
			updatePasswordDialog.show();
			break;

		case R.id.account_logout:
//			new AlertDialog.Builder(MyAccountActivity.this).setTitle("Logout").setMessage("Do you want to logout?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					GlobalData.clearGlobalData();
//					finish();
//					Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
//					startActivity(intent);
//				}
//			}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//				}
//			}).setIcon(android.R.drawable.ic_dialog_alert).show();
			MessageDialog("Logout", "Do you want to logout?");
			break;

		case R.id.account_orderhis:
			Intent i = new Intent(MyAccountActivity.this, OrderHistoryActivity.class);
			i.putExtra("userId", sharedpreferences.getString(GlobalData.Id, ""));
			startActivity(i);
			break;
		case R.id.account_outlet:
//			Intent intentOutlet = new Intent(MyAccountActivity.this, DeliveryAddressActivity.class);
//			intentOutlet.putExtra("is_view", true);
//			startActivity(intentOutlet);
			Intent address = new Intent(MyAccountActivity.this, DeliveryAddressActivity.class);
			address.putExtra("fromMyAccount", true);
			startActivity(address);
			break;
		case R.id.account_address_edit:
//			Intent address = new Intent(MyAccountActivity.this, DeliveryAddressActivity.class);
//			address.putExtra("fromMyAccount", true);
//			startActivity(address);
			break;
		case R.id.account_edit:
			if (onEditMode) {
				tvEdit.setText("EDIT");
				edtName.setBackgroundResource(R.color.transparent);
				// edtBillingAddress.setBackgroundResource(R.color.transparent);
				edtEmail.setBackgroundResource(R.color.transparent);
				edtContactNumber.setBackgroundResource(R.color.transparent);
				edtShopName.setBackgroundResource(R.color.transparent);
				edtShopAddress.setBackgroundResource(R.color.transparent);

				edtName.setEnabled(false);
				// edtBillingAddress.setEnabled(false);
				edtEmail.setEnabled(false);
				edtContactNumber.setEnabled(false);
				edtShopName.setEnabled(false);
				edtShopAddress.setEnabled(false);

				updateProfile(sharedpreferences.getString(GlobalData.Id, ""), edtName.getText().toString().trim(), edtEmail.getText().toString().trim(), edtContactNumber.getText().toString().trim(),
						edtShopName.getText().toString().trim(), edtShopAddress.getText().toString().trim());
			} else {
				tvEdit.setText("SAVE");

				edtName.setEnabled(true);
				// edtBillingAddress.setEnabled(true);
				edtEmail.setEnabled(true);
				edtContactNumber.setEnabled(true);
				edtShopName.setEnabled(true);
				edtShopAddress.setEnabled(true);

				edtName.requestFocus();
				edtName.setBackgroundResource(R.color.transparent_gray);
				// edtBillingAddress.setBackgroundResource(R.color.transparent_gray);
				edtEmail.setBackgroundResource(R.color.transparent_gray);
				edtContactNumber.setBackgroundResource(R.color.transparent_gray);
				edtShopName.setBackgroundResource(R.color.transparent_gray);
				edtShopAddress.setBackgroundResource(R.color.transparent_gray);
			}
			onEditMode = !onEditMode;

			break;

		default:
			break;
		}
	}
	
public void MessageDialog(String title, String message) {
    	
    	final Dialog dialog = new Dialog(MyAccountActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.view_message_confirm);

        ((TextView) dialog.findViewById(R.id.popup_tv_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.popup_tv_message)).setText(message);
        ImageButton imbYes = (ImageButton) dialog.findViewById(R.id.popup_imb_yes);
        ImageButton imbNo = (ImageButton) dialog.findViewById(R.id.popup_imb_no);

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
				finish();
				Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
				startActivity(intent);
            }
        });
        
        dialog.show();
    }

	public void hide_keyboard_from(Context context, View view) {
	    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
