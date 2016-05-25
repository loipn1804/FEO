package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.R;

public class OrderDialog extends Dialog {
	private OrderButtonPressListener onButtonPress;

	public OrderDialog(Context context, OrderButtonPressListener _onButtonPress) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		setContentView(R.layout.view_guest_order_popup);

		this.onButtonPress = _onButtonPress;
		final EditText edtName = (EditText) findViewById(R.id.popup_guest_name);
		final EditText edtPhone1 = (EditText) findViewById(R.id.popup_guest_phone1);
		final EditText edtPhone2 = (EditText) findViewById(R.id.popup_guest_phone2);
		final EditText edtEmail = (EditText) findViewById(R.id.popup_guest_email);

		ImageButton imbSend = (ImageButton) findViewById(R.id.popup_imb_submid);
		imbSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = edtName.getText().toString().trim();
				String phone1 = edtPhone1.getText().toString().trim();
				String phone2 = edtPhone2.getText().toString().trim();
				String email = edtEmail.getText().toString().trim();

				if (validateFields(name, phone1, phone2) && onButtonPress != null) {
					onButtonPress.onConfirmButtonPress(name, phone1, phone2, email);
				}
			}
		});
	}

	private boolean validateFields(String name, String phone1, String phone2) {
		boolean flag = true;

		if (name.isEmpty()) {
			SimpleToast.error(getContext(), "Name is required");
			flag = false;
		} else if (phone1.isEmpty()) {
			SimpleToast.error(getContext(), "Phone 1 is required");
			flag = false;
		} else if (phone1.length() < 8) {
			SimpleToast.error(getContext(), "Please fill at least 8 digits number. Thank you!");
			flag = false;
		} else if (!phone2.isEmpty() && phone2.length() < 8) {
			SimpleToast.error(getContext(), "Please fill at least 8 digits number for Phone 2. Thank you!");
			flag = false;
		}

		return flag;
	}

	public interface OrderButtonPressListener {
		public void onConfirmButtonPress(String name, String phone1, String phone2, String email);
	}
}