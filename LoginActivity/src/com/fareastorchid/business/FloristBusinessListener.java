package com.fareastorchid.business;

public interface FloristBusinessListener {
    public void onPreProcessed();

    public void onDataProcessed(Object entity);

    public void onErrorData(final ErrorMessage error_message);
}
