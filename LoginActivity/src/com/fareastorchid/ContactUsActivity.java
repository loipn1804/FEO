package com.fareastorchid;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Functions;

public class ContactUsActivity extends AbstractActivity implements OnClickListener {

	String subject = "General";
	String[] values = new String[] { "General", "Modify Order", "App Issues", "Feedback", "Others" };
	private Spinner spn;
	private TextView tvName, tvEmail, tvContactNo, tvAddress, tvPosCode, tvOrderNo, tvMessage;
	private FloristBusiness sendRequest = new FloristBusinessImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalligraphyConfig.initDefault("fonts/Gillsansmt.ttf", R.attr.fontPath);
		setContentView(R.layout.activity_contactus);

		Controller.updateActInstance(this);
		initView();
	}

	private void initView() {
		initCommonView();
		setHeaderTitle("Contact Us");
		WebView wv = (WebView) findViewById(R.id.contact_wv);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("file:///android_asset/contactus.html");

		spn = (Spinner) findViewById(R.id.contact_spn);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn.setAdapter(dataAdapter);
		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				subject = values[pos];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		Button send = (Button) findViewById(R.id.contact_send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validateFields())
					doSendContact();
			}
		});

		tvName = (TextView) findViewById(R.id.contact_name);
		tvEmail = (TextView) findViewById(R.id.contact_email);
		tvContactNo = (TextView) findViewById(R.id.contact_no);
		tvAddress = (TextView) findViewById(R.id.contact_address);
		tvPosCode = (TextView) findViewById(R.id.contact_postal);
		tvOrderNo = (TextView) findViewById(R.id.contact_order);
		tvMessage = (TextView) findViewById(R.id.contact_mess);

		tvAddress.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.contact_address) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }
				return false;
			}
		});
		
		tvMessage.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.contact_mess) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }
				return false;
			}
		});
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	private boolean validateFields() {
		boolean flag = true;
		if (tvName.getText().toString().trim().isEmpty()) {
			SimpleToast.error(ContactUsActivity.this, "Name is required");
			flag = false;
		} else if (tvEmail.getText().toString().trim().isEmpty()) {
			SimpleToast.error(ContactUsActivity.this, "Email Address is required");
			flag = false;
		} else if (tvContactNo.getText().toString().trim().isEmpty()) {
			SimpleToast.error(ContactUsActivity.this, "Contact Number is required");
			flag = false;
		} else if (tvMessage.getText().toString().trim().isEmpty()) {
			SimpleToast.error(ContactUsActivity.this, "Message is required");
			flag = false;
		}

		return flag;
	}

	private void doSendContact() {
		sendRequest.setBusinessListener(new FloristBusinessListener() {
			String message = "";

			@Override
			public void onErrorData(final ErrorMessage error_message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						hideProgressBar();
						if (!error_message.getError_message().equalsIgnoreCase(""))
							SimpleToast.error(Controller.getActInstance(ContactUsActivity.class), error_message.getError_message());
						else {
							SimpleToast.error(Controller.getActInstance(ContactUsActivity.class), R.string.err_connect);
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
								Functions.showDialogMessage(message, ContactUsActivity.this);
							else {
								SimpleToast.ok(ContactUsActivity.this, "Your message has been sent successfully!");
							}
							finish();
						}
					}

				});
			}
		});

		sendRequest.sendContact(tvName.getText().toString(), tvEmail.getText().toString(), tvContactNo.getText().toString(), tvAddress.getText().toString(), tvPosCode.getText().toString(), subject,
				tvOrderNo.getText().toString(), tvMessage.getText().toString());
	}
}
