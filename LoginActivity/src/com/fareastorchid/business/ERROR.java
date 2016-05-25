package com.fareastorchid.business;

public class ERROR {
    public final static String UNKNOWN_MSG = "Unknown error, please try again later!";
    public final static String NETWORK_ERROR_MSG = "No internet connection. (50001)";// "NETWORK_ERROR";
    public final static String SOCKET_NOT_CONNECT_MSG = "No internet connection. (50002)";// "Socket is not connect";
    public final static String SOCKET_TIME_OUT_MSG = "Internet connection is not stable, please try again later. (50003)";// "Request
    // socket
    // time
    // out

    public final static int SESSION_EXPIRE = 102;
    public final static int UNKNOWN = 502;
    // Socket error
    public final static int WRONG_USER = 50000;
    public final static int NETWORK_ERROR = 50001;
    public final static int SOCKET_NOT_CONNECT = 50002;
    public final static int SOCKET_TIME_OUT = 50003;
}