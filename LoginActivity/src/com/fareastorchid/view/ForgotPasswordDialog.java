package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.R;

public class ForgotPasswordDialog extends Dialog {
    private ForgotButtonPressListener onButtonPress;

    public ForgotPasswordDialog(Context context, ForgotButtonPressListener _onButtonPress) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_forgot_pwd_popup);

        this.onButtonPress = _onButtonPress;
        final EditText edtEmail = (EditText) findViewById(R.id.popup_edittext);
        ImageButton imbSend = (ImageButton) findViewById(R.id.popup_imb_send);
        imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onButtonPress != null) {
                    onButtonPress.onForgotButtonPress(edtEmail.getText().toString());
                }
            }
        });
    }

    public interface ForgotButtonPressListener {
        public void onForgotButtonPress(String email);
    }
}