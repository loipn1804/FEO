package com.fareastorchid.business;

public class ErrorMessage {
    private int error_code = -9999;
    private String error_message = "";

    public ErrorMessage(int error_code, String error_message) {
        this.error_code = error_code;
        this.error_message = error_message;

        switch (this.error_code) {
            case ERROR.NETWORK_ERROR:
                this.error_message = ERROR.NETWORK_ERROR_MSG;
                break;

            default:
                break;
        }
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
