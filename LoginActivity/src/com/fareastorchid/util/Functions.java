package com.fareastorchid.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.fareastorchid.MainActivity;
import com.fareastorchid.view.MessageDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Functions {

    public static String getSerialNo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = telephonyManager.getDeviceId();

        if (deviceID != null && !deviceID.equals(""))
            return deviceID;
        /**
         * if there's no imei then use mac address of wifi
         */
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();

        if (macAddress != null && !macAddress.equals(""))
            return macAddress;

        return "";
        // return "355136056633517";
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(String input) {
        String newString = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(input);
            newString = new SimpleDateFormat("MMM dd, yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newString;
    }

    public static void showDialogMessage(String msg, Context context) {
        new MessageDialog(context, "", msg, false).show();
    }

    public static void showDialogMessageAndFinish(String msg, final Activity context) {
        new MessageDialog(context, "", msg, false).show();
    }

    public static void showDialogMessageAndToMain(String msg, final Activity context) {
        new AlertDialog.Builder(context).setMessage(msg).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra("reloadItem", true);
                context.startActivity(mainIntent);
                context.finish();
                return;
            }
        }).show();
    }

    public static void showErrorDialogMessage(final String msg, final Activity context) {
        Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        }).show();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public static String round2String(double value) {
        String result = String.format("%.2f", value);
        return result;
    }

    public static String round2String(String value) {
        String result = "0.00";
        try {
            result = String.format("%.2f", Double.valueOf(value));
        } catch (NumberFormatException e) {
        }

        return result;
    }
}
