package com.fareastorchid.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fareastorchid.R;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnSpecialRequestListener;
import com.fareastorchid.util.PQT;

public class UpdateSpecialRequestDialog extends Dialog {

    private OnSpecialRequestListener onButtonPress;

    private EditText edtSpecialRequest, edtQuantity;
    private TextView popupTitle;
    private ImageButton imbSend;

    public UpdateSpecialRequestDialog(final Context context, final int position) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.view_update_specialrequest_popup);

        this.onButtonPress = (OnSpecialRequestListener) context;

        this.edtSpecialRequest = (EditText) findViewById(R.id.popup_edt_specailrequest);
        this.edtQuantity = (EditText) findViewById(R.id.popup_edt_quantity);
        this.popupTitle = (TextView) findViewById(R.id.popup_title);

        if (position >= 0 && PQT.isListDirty(GlobalData.listOrderItem)) {
            this.edtSpecialRequest.setText(GlobalData.listOrderItem.get(
                    position).getSpecReq());
            this.edtQuantity.setText(String.valueOf(GlobalData.listOrderItem
                    .get(position).getQuantity()));

            this.popupTitle.setText(GlobalData.listOrderItem.get(position)
                    .getItem().getItem_name());
        }

        this.imbSend = (ImageButton) findViewById(R.id.popup_imb_send);

        this.imbSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String quantity = edtQuantity.getText().toString();

                if (quantity.equals(""))
                    quantity = "0";

                if (onButtonPress != null) {
                    onButtonPress.onPostUpdateSpecialRequest(position,
                            edtSpecialRequest.getText().toString(),
                            Integer.valueOf(quantity));
                }
            }
        });
    }
}