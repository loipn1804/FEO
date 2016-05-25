package com.fareastorchid.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.MainActivity;
import com.fareastorchid.R;
import com.fareastorchid.model.controller.Controller;

import br.com.pierry.simpletoast.SimpleToast;

public class UpdatePasswordDialog extends Dialog {
	private final EditText edtCurPwd, edtNewPwd, edtConfirmNewPwd;
	private ChangePwdButtonPressListener onButtonPress;
	private ImageButton imbSend;

	public UpdatePasswordDialog(final Context context,
			ChangePwdButtonPressListener _onButtonPress) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		setContentView(R.layout.view_change_pwd_popup);

		this.onButtonPress = _onButtonPress;

		edtCurPwd = (EditText) findViewById(R.id.popup_edt_cur_pass);
		edtNewPwd = (EditText) findViewById(R.id.popup_edt_new_pass);
		edtConfirmNewPwd = (EditText) findViewById(R.id.popup_edt_confirm_new_pass);
		imbSend = (ImageButton) findViewById(R.id.popup_imb_send);

		imbSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (edtCurPwd.getText().toString().isEmpty()) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Please input your current password");
				} else if (edtNewPwd.getText().toString().isEmpty()) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Please input your new password");
				} else if (!validatePwd(edtNewPwd.getText().toString())) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Password must be 6 to 8 alphanumberic");
				} else if (!edtConfirmNewPwd.getText().toString()
						.equals(edtNewPwd.getText().toString())) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							"Confirm password and new password does not match");
				} else if (onButtonPress != null) {
					onButtonPress.onChangePwdButtonPress(edtCurPwd.getText()
							.toString(), edtNewPwd.getText().toString());
				}
			}
		});
	}

	private boolean validatePwd(String password) {
		String pattern = "^[a-zA-Z0-9]{6,8}$";

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(password);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public interface ChangePwdButtonPressListener {
		public void onChangePwdButtonPress(String curPwd, String newPwd);
	}
}