package com.fareastorchid.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.fareastorchid.MainActivity;
import com.fareastorchid.model.controller.Controller;

public class Connectivity {
    /*
     * HACKISH: These constants aren't yet available in my API level (7), but I
     * need to handle these cases if they come up, on newer versions
     */
    public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
    public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
    public static final int NETWORK_TYPE_IDEN = 11; // Level 8
    public static final int NETWORK_TYPE_LTE = 13; // Level 11

    public static synchronized boolean isNetworkAvailable(boolean isShowToast, Activity activity) {
        try {
            if (isConnected(activity)) {
                return true;
            }

            if (isShowToast) {
                // Utils.showErrorNetWork();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    // public static boolean isConnected(Context context){
    // ConnectivityManager cm = (ConnectivityManager)
    // context.getSystemService(Context.CONNECTIVITY_SERVICE);
    // NetworkInfo info = cm.getActiveNetworkInfo();
    // return (info != null && info.isConnected());
    // }
    public static boolean isConnected(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // Skip if no connection, or background data disabled
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }

        // Only update if WiFi or 3G is connected and not roaming
        int netType = info.getType();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE
                // && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !mTelephony.isNetworkRoaming()) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            System.out.println("CONNECTED VIA WIFI");
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                // NOT AVAILABLE YET IN API LEVEL 7
                case Connectivity.NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps
                case Connectivity.NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps
                case Connectivity.NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 Mbps
                case Connectivity.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps
                case Connectivity.NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public static String getTypeOfConnection() {
        try {
            ConnectivityManager connectionManager = (ConnectivityManager) Controller.getActInstance(MainActivity.class).getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectionManager == null) {
                return "UNKOWN";
            }

            NetworkInfo info = connectionManager.getActiveNetworkInfo();

            if (info == null) {
                return "UNKNOWN";
            }

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else if (info.getType() == ConnectivityManager.TYPE_WIMAX) {
                return "WIMAX";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getTypeOfConnectionMobile();
            }

        } catch (Exception e) {
        }
        return "UNKNOWN";
    }

    public static String getTypeOfConnectionMobile() {
        String type = "MOBILE UNKNOWN";
        try {
            TelephonyManager teleMan = (TelephonyManager) Controller.getActInstance(MainActivity.class).getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = teleMan.getNetworkType();

            switch (networkType) {
                case 7:
                    type = "1xRTT";
                    break;
                case 4:
                    type = "CDMA";
                    break;
                case 2:
                    type = "EDGE";
                    break;
                case 14:
                    type = "eHRPD";
                    break;
                case 5:
                    type = "EVDO rev. 0";
                    break;
                case 6:
                    type = "EVDO rev. A";
                    break;
                case 12:
                    type = "EVDO rev. B";
                    break;
                case 1:
                    type = "GPRS";
                    break;
                case 8:
                    type = "HSDPA";
                    break;
                case 10:
                    type = "HSPA";
                    break;
                case 15:
                    type = "HSPA+";
                    break;
                case 9:
                    type = "HSUPA";
                    break;
                case 11:
                    type = "iDen";
                    break;
                case 13:
                    type = "LTE";
                    break;
                case 3:
                    type = "UMTS";
                    break;
                case 0:
                    type = "MOBILE UNKNOWN";
                    break;
            }
        } catch (Exception e) {
        }
        return type;
    }

    public static int getTypeOfConnMobile() {
        int type = 5;// "MOBILE UNKNOWN";
        try {
            TelephonyManager teleMan = (TelephonyManager) Controller.getActInstance(MainActivity.class).getSystemService(Context.TELEPHONY_SERVICE);
            type = teleMan.getNetworkType();

            if (type == 0)
                type = 5;// "MOBILE UNKNOWN";
        } catch (Exception e) {
        }
        return type;
    }

    @SuppressLint("DefaultLocale")
    public static String getOperatorName() {
        String carrierName = "UNKNOWN";
        try {
            TelephonyManager manager = (TelephonyManager) Controller.getActInstance(MainActivity.class).getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getNetworkOperatorName().toUpperCase();
        } catch (Exception e) {
        }
        return carrierName;
    }

    public static boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 m.laban.vn");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}