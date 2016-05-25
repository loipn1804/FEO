package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.R;

public class SpecRequestDialog extends Dialog {
    private ConfirmButtonPressListener onButtonPress;

    public SpecRequestDialog(Context context, ConfirmButtonPressListener _onButtonPress) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_spec_req_popup);

        this.onButtonPress = _onButtonPress;
        final EditText edtEmail = (EditText) findViewById(R.id.popup_edittext);
        ImageButton imbSend = (ImageButton) findViewById(R.id.popup_imb_send);
        imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onButtonPress != null) {
                    onButtonPress.onYesButtonPress(edtEmail.getText().toString());
                }
            }
        });
    }

    public interface ConfirmButtonPressListener {
        public void onYesButtonPress(String req);
    }
}