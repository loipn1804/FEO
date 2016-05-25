package com.fareastorchid;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.KeyboardVisibilityListener;
import com.fareastorchid.listener.KeyboardVisibilityListener.OnKeyboardVisibilityListener;
import com.fareastorchid.model.UserInfo;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;
import com.fareastorchid.view.ForgotPasswordDialog;
import com.fareastorchid.view.ForgotPasswordDialog.ForgotButtonPressListener;
import com.fareastorchid.view.RegisterDialog;
import com.fareastorchid.view.RegisterDialog.RegisterButtonPressListener;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends AbstractActivity implements ForgotButtonPressListener, RegisterButtonPressListener, OnKeyboardVisibilityListener {
	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private SharedPreferences sharedpreferences;
	private ForgotPasswordDialog forgotPasswordDialog;
	private RegisterDialog registerDialog;
	private FloristBusiness doResetPW = new FloristBusinessImpl();
	private FloristBusiness doRegister = new FloristBusinessImpl();
	private FloristBusiness getLogin = new FloristBusinessImpl();

	private ScrollView scrollView;
	private LinearLayout lnlBlockBottom;
	private KeyboardVisibilityListener keyboardVisibilityListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Mint.initAndStartSession(LoginActivity.this, "f4509871");
		Controller.updateActInstance(this);
		setContentView(R.layout.activity_login);

		scrollView = (ScrollView) findViewById(R.id.login_form);
		lnlBlockBottom = (LinearLayout) findViewById(R.id.lnlBlockBottom);
		keyboardVisibilityListener = new KeyboardVisibilityListener();
		keyboardVisibilityListener.setKeyboardListener(this, LoginActivity.this, R.id.rootview);

		sharedpreferences = getSharedPreferences(GlobalData.UserPREFERENCES, Context.MODE_PRIVATE);
		if (GlobalData.isLogin) {
			// if logged in then do not allow user stay here anymore
			finish();
		}

		Functions.showDialogMessage("For trader, kindly login before placing an order", LoginActivity.this);

		initCommonView();
		// Set up the login form.
		mUsername = sharedpreferences.getString(GlobalData.UserName, "");
		mUsernameView = (EditText) findViewById(R.id.username);
		mUsernameView.setText(mUsername);
		mUsernameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mUsernameView.setError(null);
			}
		});

		findViewById(R.id.login_imv_forgot_pwd).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				forgotPasswordDialog = new ForgotPasswordDialog(LoginActivity.this, LoginActivity.this);
				forgotPasswordDialog.show();
			}
		});

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setTypeface(Typeface.DEFAULT);

		mPasswordView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mUsernameView.setError(null);
			}
		});
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		findViewById(R.id.login_imb_login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// attempt login
				if (!GlobalData.isLogin)
					attemptLogin();
				else {
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				}
			}
		});

		findViewById(R.id.login_imv_login_later).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}
		});

		findViewById(R.id.login_imb_signup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// sendEmail();
				registerDialog = new RegisterDialog(LoginActivity.this, LoginActivity.this);
				registerDialog.show();
			}
		});
	}

	private void resetPW(String email) {
		doResetPW.setBusinessListener(new FloristBusinessListener() {
			String message = "";

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), R.string.err_connect);
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
							message = root.getString("message");
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							hideProgressBar();
							if (!message.isEmpty())
								Functions.showDialogMessage(message, LoginActivity.this);
						}
					}

				});
			}
		});

		doResetPW.userFeoResetPWCode(email);
	}

	private void register(String name, String contactDetail) {
		doRegister.setBusinessListener(new FloristBusinessListener() {
			String message = "";

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), R.string.err_connect);
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
							message = root.getString("message");
						} catch (JSONException e1) {
							e1.printStackTrace();
						} finally {
							hideProgressBar();
							if (!message.isEmpty())
								Functions.showDialogMessage(message, LoginActivity.this);
						}
					}

				});
			}
		});

		doRegister.sendRegister(name, contactDetail);
	}

	protected void sendEmail() {
		String[] TO = { "thuanpq@latsolutions.com" };
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "anders.tan@ymail.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "eragon009nt@gmail.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "tinhocgocong@gmail.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "Mobile@fareastorchid.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "Mobileappfeo@gmail.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "Mobileappteam@fareastorchid.com" });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { "Mobileappteam@hotmail.com" });
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SignUp Florist");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "+ Name:  \n+Contact Details:");

		try {
			startActivity(Intent.createChooser(emailIntent, "Sign up by ..."));
			finish();
			Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(LoginActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// if (mUsername.equals(" ") || mUsername.equals("khoint")) {
		// mUsername = "khoint";
		// mPassword = "123456";
		// }

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(Html.fromHtml("<font color='red'>This field is required</font>"));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(Html.fromHtml("<font color='red'>This password is too short</font>"));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid username.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(Html.fromHtml("<font color='red'>This field is required</font>"));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			Log.w("mEmail ", mUsername + " mPassword =" + mPassword);
			getLogin(mUsername, mPassword);
		}
	}

	private void getLogin(String username, String password) {
		getLogin.setBusinessListener(new FloristBusinessListener() {

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.w("getLogin", "onErrorData");
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(LoginActivity.class), R.string.err_connect);
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
							JSONObject data = root.getJSONObject("data");
							if (data.length() != 0) {
								try {
									UserInfo userInfo = new UserInfo(data);
									saveUserInfo(userInfo);
									GlobalData.clearGlobalData();
									GlobalData.isLogin = true;
									Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
									mainIntent.putExtra("reloadItem", true);
									startActivity(mainIntent);
									finish();
								} catch (Exception e) {
									e.printStackTrace();

									Log.w("getLogin", "Exception");
								}
							}
						} catch (JSONException e1) {

							Log.w("getLogin", "JSONException");
							e1.printStackTrace();
						} finally {
							hideProgressBar();
						}
					}
				});

			}
		});
		getLogin.userFeoLogin(username, password);
	}

	private void saveUserInfo(UserInfo userInfo) {
		Editor editor = sharedpreferences.edit();
		editor.putString(GlobalData.FullName, userInfo.getFullName());
		editor.putString(GlobalData.Id, userInfo.getId());
		editor.putString(GlobalData.Email, userInfo.getEmail());
		editor.putString(GlobalData.UserName, userInfo.getUsername());
		editor.putString(GlobalData.Status, userInfo.getStatus());
		editor.putString(GlobalData.ShopName, userInfo.getShop_name());
		editor.putString(GlobalData.Office_No, userInfo.getOfficeNo());
		editor.putString(GlobalData.BillingAddress, userInfo.getBilling_address());
		editor.putString(GlobalData.ContactNumber, userInfo.getContact_number());
		editor.putString(GlobalData.Level, userInfo.getLevel());
		editor.putString(GlobalData.Position, userInfo.getPosition());
		editor.putString(GlobalData.Outlet, userInfo.getOutlet());
		editor.commit();
	}

	@Override
	public void onForgotButtonPress(String email) {
		if (forgotPasswordDialog != null && forgotPasswordDialog.isShowing())
			forgotPasswordDialog.dismiss();

		resetPW(email);
	}

	@Override
	public void onRegisterButtonPress(String name, String contact) {
		if (registerDialog != null && registerDialog.isShowing()) {
			registerDialog.dismiss();
		}
		register(name, contact);
	}

	@Override
	public void onVisibilityChanged(boolean visible) {
		if (visible) {
			if (lnlBlockBottom.getVisibility() == View.VISIBLE) {
				lnlBlockBottom.setVisibility(View.GONE);
				// scrollView.fullScroll(View.FOCUS_DOWN);
			}
		} else {
			if (lnlBlockBottom.getVisibility() == View.GONE) {
				scrollView.fullScroll(View.FOCUS_UP);
				lnlBlockBottom.setVisibility(View.VISIBLE);
			}
		}
	}
}
