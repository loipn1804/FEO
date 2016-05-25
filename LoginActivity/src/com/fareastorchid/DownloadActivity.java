package com.fareastorchid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fareastorchid.util.Configuration;
import com.fareastorchid.util.FileDialog;
import com.fareastorchid.util.Utils;

public class DownloadActivity extends Activity implements OnClickListener {

	private static final int REQUEST_SELECT_DOWNLOAD_DIR = 1111;
	private TextView tvFolder;
	private EditText edtName;
	private ImageView ibSelect;
	private TextView ibSubmid;
	private String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setContentView(R.layout.view_download_dialog);

		Intent i = getIntent();
		Configuration.initialize(DownloadActivity.this);
		fileName = i.hasExtra("file_name") ? i.getStringExtra("file_name") : "";
		edtName = (EditText) findViewById(R.id.download_name);
		tvFolder = (TextView) findViewById(R.id.download_folder);
		tvFolder.setText(Configuration.getDownloadDirPath());
		ibSubmid = (TextView) findViewById(R.id.download_submid);
		ibSelect = (ImageView) findViewById(R.id.download_select);
		ibSelect.setOnClickListener(this);
		ibSubmid.setOnClickListener(this);
		edtName.setText(fileName);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_SELECT_DOWNLOAD_DIR:
			if (resultCode == RESULT_OK) {
				String downPath = data.getStringExtra(FileDialog.RESULT_PATH);
				Configuration.setDowloadPath(downPath);
				tvFolder.setText(downPath);
				// Utils.downloadFile(downPath,
				// data.substring(data.lastIndexOf("/")));
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.download_select:
			if (Utils.checkCardState(this, true)) {
				Intent intent = new Intent(getBaseContext(), FileDialog.class);
				intent.putExtra(FileDialog.START_PATH, Configuration.getDownloadDirPath());
				intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
				startActivityForResult(intent, REQUEST_SELECT_DOWNLOAD_DIR);
			}
			break;
		case R.id.download_submid:
			Intent returnIntent = new Intent();
			returnIntent.putExtra("folder", tvFolder.getText().toString());
			returnIntent.putExtra("filename", edtName.getText().toString());
			setResult(RESULT_OK, returnIntent);
			finish();
			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			//
			//
			// // finish();
			// }
			// });
			// finish();
			break;
		}
	}

}
