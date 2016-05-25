package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fareastorchid.R;

public class ConfirmDialog extends Dialog {
    public ConfirmDialog(Context context, final ConfirmButtonPressListener _onButtonPress, final int extraData, final String title, final String message) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_confirm_popup);

        ((TextView) findViewById(R.id.popup_tv_title)).setText(title);

        ((TextView) findViewById(R.id.popup_tv_message)).setText(message);

        ImageButton imbYes = (ImageButton) findViewById(R.id.popup_imb_yes);
        ImageButton imbNo = (ImageButton) findViewById(R.id.popup_imb_no);

        imbYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (_onButtonPress != null) {
                    _onButtonPress.onConfirmButtonPress(true, extraData);
                }

                ConfirmDialog.this.dismiss();
            }
        });

        imbNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (_onButtonPress != null) {
                    _onButtonPress.onConfirmButtonPress(false, extraData);
                }

                ConfirmDialog.this.dismiss();
            }
        });
    }

    public interface ConfirmButtonPressListener {
        public void onConfirmButtonPress(boolean isYes, int extraData);
    }
}