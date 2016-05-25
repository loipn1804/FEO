package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.R;
import com.fareastorchid.data.GlobalData;

public class EditAddressDialog extends Dialog {
    private EditButtonPressListener onButtonPress;

    public EditAddressDialog(Context context, EditButtonPressListener _onButtonPress) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_edit_address);

        this.onButtonPress = _onButtonPress;
        final EditText edtAddress = (EditText) findViewById(R.id.popup_edit_address);
        edtAddress.setText(GlobalData.edtAddress.getAddress());
        ImageButton imbSend = (ImageButton) findViewById(R.id.popup_edit_submid);
        imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onButtonPress != null) {
                    if (!edtAddress.getText().toString().equalsIgnoreCase(""))
                        onButtonPress.onConfirmButtonPress(edtAddress.getText().toString());
                }
            }
        });
    }

    public interface EditButtonPressListener {
        public void onConfirmButtonPress(String address);
    }
}