package com.fareastorchid.connection;

import com.fareastorchid.business.ErrorMessage;

import org.json.JSONObject;


public interface RequestListener {
    public void onRequestComplete(JSONObject object);

    public void onRequestFailed(ErrorMessage error_message);
}
