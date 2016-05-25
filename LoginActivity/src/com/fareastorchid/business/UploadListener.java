package com.fareastorchid.business;

public interface UploadListener {
    public void onDataProcessed(Object entity);

    public void onErrorData(ErrorMessage error_message);

    public void progress(long l, String des);

}