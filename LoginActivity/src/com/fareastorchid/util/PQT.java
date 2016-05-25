package com.fareastorchid.util;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class PQT {
    public static boolean isListDirty(List<?> in) {
        boolean result = false;

        if (in != null && in.size() > 0) {
            result = true;
        }

        return result;
    }

    public static boolean isStringDirty(String in) {
        boolean result = false;

        if (in != null && !in.isEmpty()) {
            result = true;
        }

        return result;
    }

    public static boolean isNotNull(Object in) {
        return (in != null);
    }

    public static boolean isNotNull(Object... in) {
        for (Object obj : in) {
            if (obj == null)
                return false;
        }

        return true;
    }

    public static boolean isNull(Object in) {
        return (in == null);
    }

    public static Object getValueIfNull(Object in, Object valIfNull, Object valIfNotNull) {
        Object result = in;

        if (isNotNull(in)) {
            if (valIfNotNull == null)
                result = in;
            else
                result = valIfNotNull;
        } else {
            result = valIfNull;
        }

        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View focus = activity.getCurrentFocus();
                if (focus != null)
                    inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                else
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }

        }
    }

    public static String numToMonth(int num) {
        String month = "";
        switch (num) {
            case 1:
                month = " January ";
                break;
            case 2:
                month = " February ";
                break;
            case 3:
                month = " March ";
                break;
            case 4:
                month = " April ";
                break;
            case 5:
                month = " May ";
                break;
            case 6:
                month = " June ";
                break;
            case 7:
                month = " July ";
                break;
            case 8:
                month = " August ";
                break;
            case 9:
                month = " September ";
                break;
            case 10:
                month = " October ";
                break;
            case 11:
                month = " November ";
                break;
            case 12:
                month = " December ";
                break;
            default:
                break;
        }
        return month;
    }

    public static String numToMonth2(int num) {
        String month = "";
        switch (num) {
            case 1:
                month = " JAN";
                break;
            case 2:
                month = " FEB";
                break;
            case 3:
                month = " MAR";
                break;
            case 4:
                month = " APR";
                break;
            case 5:
                month = " MAY";
                break;
            case 6:
                month = " JUN";
                break;
            case 7:
                month = " JUL";
                break;
            case 8:
                month = " AUG";
                break;
            case 9:
                month = " SEP";
                break;
            case 10:
                month = " OCT";
                break;
            case 11:
                month = " NOV";
                break;
            case 12:
                month = " DEC";
                break;
            default:
                break;
        }
        return month;
    }
}