package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fareastorchid.R;

public class AddAddressDialog extends Dialog {
    private AddAddressDialogListener onButtonPress;

    public AddAddressDialog(Context context, AddAddressDialogListener _onButtonPress) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_add_address);

        this.onButtonPress = _onButtonPress;
        final EditText edtAddress = (EditText) findViewById(R.id.popup_add_address);
        ImageButton imbSend = (ImageButton) findViewById(R.id.popup_add_submid);
        imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onButtonPress != null) {
                    if (!edtAddress.getText().toString().equalsIgnoreCase(""))
                        onButtonPress.onConfirmAddButtonPress(edtAddress.getText().toString());
                }
            }
        });
    }

    public interface AddAddressDialogListener {
        public void onConfirmAddButtonPress(String address);
    }
}