package com.fareastorchid.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fareastorchid.R;

public class MessageDialog extends Dialog {

	public MessageDialog(final Context context, final String title, final String message, final boolean isFinish) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_message_popup);

        // ((TextView) findViewById(R.id.popup_tv_title)).setText(title);

        ((TextView) findViewById(R.id.popup_tv_message)).setText(message);
        ((TextView) findViewById(R.id.popup_tv_message)).setMovementMethod(new ScrollingMovementMethod());
        ImageButton imbYes = (ImageButton) findViewById(R.id.popup_imb_yes);

        imbYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MessageDialog.this.dismiss();
                if (isFinish) {
                    ((Activity) context).finish();
                }
            }
        });
    }
}