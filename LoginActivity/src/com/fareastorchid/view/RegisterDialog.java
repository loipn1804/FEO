package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.R;

public class RegisterDialog extends Dialog {
    private RegisterButtonPressListener onButtonPress;

    public RegisterDialog(Context context, RegisterButtonPressListener _onButtonPress) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_register_dialog);

        this.onButtonPress = _onButtonPress;
        final EditText edtName = (EditText) findViewById(R.id.register_name);
        final EditText edtContact = (EditText) findViewById(R.id.register_contact);
        ImageButton imbSend = (ImageButton) findViewById(R.id.register_submid);
        imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onButtonPress != null) {
                    onButtonPress.onRegisterButtonPress(edtName.getText().toString(), edtContact.getText().toString());
                }
            }
        });
    }

    public interface RegisterButtonPressListener {
        public void onRegisterButtonPress(String name, String contact);
    }
}