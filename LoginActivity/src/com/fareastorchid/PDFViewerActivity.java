package com.fareastorchid;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.data.GlobalData;
import com.fareastorchid.model.controller.Controller;

public class PDFViewerActivity extends BaseActivity {

	private static final int REQUEST_SELECT_DOWNLOAD_DIR = 1111;
	WebView webView;
	ImageButton imbDownload;
	String url;
	String category;
	String subCategory;
	String base_url;
	String folder, filename;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdfviewer);

		mProgressbar = (ProgressBar) findViewById(R.id.progressbar);

		Intent i = getIntent();
		base_url = i.getStringExtra("url");

		category = i.getStringExtra("category");
		subCategory = i.getStringExtra("subCategory");

		// url = "https://drive.google.com/viewerng/viewer?embedded=true&url="
		url = "https://drive.google.com/gview?embedded=true&url=" + base_url;
		webView = (WebView) findViewById(R.id.wvTest);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				Log.e("ThuanPQ", "onPageStarted");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				hideProgressBar();
				Log.e("ThuanPQ", "onPageFinished");
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				hideProgressBar();
				Log.e("ThuanPQ", "onReceivedError" + errorCode);
			}
		});
		showProgressBar();

		Handler han = new Handler();
		han.postDelayed(new Runnable() {

			@Override
			public void run() {

				webView.loadUrl(url);
			}
		}, 2000);

		imbDownload = (ImageButton) findViewById(R.id.price_list_download);
		imbDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(PDFViewerActivity.this,
						DownloadActivity.class);
				// i.putExtra("file_name",
				// base_url.substring(base_url.lastIndexOf("/") + 1));
				if (category.equalsIgnoreCase("country shipment")) {
					i.putExtra(
							"file_name",
							base_url.substring(base_url.lastIndexOf("/") + 1,
									base_url.lastIndexOf(")") + 1)
									+ " "
									+ subCategory + ".pdf");
					i.putExtra("url", base_url);
				} else {
					i.putExtra("file_name", getDate() + " " + category + ".pdf");
					i.putExtra("url", base_url);
				}

				startActivityForResult(i, REQUEST_SELECT_DOWNLOAD_DIR);
			}
		});
	}

	private String getDate() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int year = calendar.get(Calendar.YEAR);
		int date = calendar.get(Calendar.DATE);

		SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
		String month_name = month_date.format(calendar.getTime());

		return date + " " + month_name + " " + year;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_SELECT_DOWNLOAD_DIR:
			if (resultCode == RESULT_OK) {
				folder = data.getStringExtra("folder");
				filename = data.getStringExtra("filename");
				final DownloadTask downloadTask = new DownloadTask(
						PDFViewerActivity.this);
				base_url = base_url.replace(" ", "%20");
				downloadTask.execute(base_url);

			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.w("onBackPressed", "onBackPressed");
		GlobalData.isFromPDFview = true;
	}

	@Override
	public void showProgressBar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(ProgressBar.VISIBLE);
		}
	}

	// public void downloadFile(String file_url, String folder_url, String
	// file_name) {
	// try {
	//
	// // SimpleToast.info(Controller.getActInstance(MainActivity.class),
	// "Downloading ...");
	// showProgressBar();
	//
	// URL url = new URL(file_url);
	//
	// // create the new connection
	//
	// HttpURLConnection urlConnection = (HttpURLConnection)
	// url.openConnection();
	//
	// // set up some things on the connection
	//
	// urlConnection.setRequestMethod("GET");
	//
	// urlConnection.setDoOutput(true);
	//
	// // and connect!
	//
	// urlConnection.connect();
	//
	// // set the path where we want to save the file
	//
	// // in this case, going to save it on the root directory of the
	//
	// // sd card.
	//
	// File SDCardRoot = new File(folder_url);
	//
	// // create a new file, specifying the path, and the filename
	//
	// // which we want to save the file as.
	//
	// File file = new File(SDCardRoot, file_name);
	//
	// // this will be used to write the downloaded data into the file we
	// // created
	//
	// FileOutputStream fileOutput = new FileOutputStream(file);
	//
	// // this will be used in reading the data from the internet
	//
	// InputStream inputStream = urlConnection.getInputStream();
	//
	// // this is the total size of the file
	// // variable to store total downloaded bytes
	// // create a buffer...
	//
	// byte[] buffer = new byte[1024];
	//
	// int bufferLength = 0; // used to store a temporary size of the
	// // buffer
	//
	// // now, read through the input buffer and write the contents to the
	// // file
	//
	// while ((bufferLength = inputStream.read(buffer)) > 0) {
	// fileOutput.write(buffer, 0, bufferLength);
	// }
	//
	// // close the output stream when done
	// hideProgressBar();
	// SimpleToast.info(Controller.getActInstance(MainActivity.class),
	// "Download complete");
	// fileOutput.close();
	// } catch (Exception e) {
	// SimpleToast.error(Controller.getActInstance(MainActivity.class),
	// "Download fail");
	// e.printStackTrace();
	// }
	// }

	@Override
	public void hideProgressBar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(ProgressBar.GONE);
		}
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP "
							+ connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				String file_path = folder + "/" + filename;
				Log.w("paht = ", file_path);
				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(file_path);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			showProgressBar();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			// mProgressDialog.setIndeterminate(false);
			// mProgressDialog.setMax(100);
			// mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			mWakeLock.release();
			hideProgressBar();

			if (result != null)
				SimpleToast.error(
						Controller.getActInstance(MainActivity.class),
						"Download fail");
			else
				SimpleToast.info(Controller.getActInstance(MainActivity.class),
						"Download complete");
		}
	}
}
