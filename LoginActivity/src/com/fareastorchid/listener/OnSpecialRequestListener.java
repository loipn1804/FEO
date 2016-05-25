package com.fareastorchid.listener;

public interface OnSpecialRequestListener {

    public void onUpdateSpecialRequest(int orderItemPos);

    public void onPostUpdateSpecialRequest(int orderItemPos,
                                           String specialRequest, int quantity);
}
