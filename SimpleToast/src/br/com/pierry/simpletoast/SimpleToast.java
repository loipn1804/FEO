package br.com.pierry.simpletoast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class SimpleToast {

	public static void ok(Context context, String msg) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_ok, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(msg);

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}

	public static void ok(Context context, int msg_id) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_ok, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(context.getString(msg_id));

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}

	public static void error(Context context, String msg) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_error, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(msg);

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}

	public static void error(Context context, int msg_id) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_error, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(context.getString(msg_id));

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}

	public static void info(Context context, String msg) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_info, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(msg);

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}

	public static void info(Context context, int msg_id) {
		LayoutInflater myInflater = LayoutInflater.from(context);
		View view = myInflater.inflate(R.layout.toast_info, null);

		Button button = (Button) view.findViewById(R.id.button);
		button.setText(context.getString(msg_id));

		Toast mytoast = new Toast(context);

		mytoast.setView(view);
		mytoast.setDuration(Toast.LENGTH_SHORT);
		mytoast.show();
	}
}
