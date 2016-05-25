package com.fareastorchid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

public class Configuration {
    public static final String TAG = "Configuration";

    /**
     * Preferences.
     */
    // public static final String PREFERENCES_FIRST_TIME_RUN= "FirstRun";
    // public static final String PREFERENCES_GENERAL_HOME_PAGE =
    // "GeneralHomePage";
    // public static final String PREFERENCES_GENERAL_SEARCH_URL =
    // "GeneralSearchUrl";
    // public static final String PREFERENCES_GENERAL_SWITCH_TABS_METHOD =
    // "GeneralSwitchTabMethod";
    // public static final String PREFERENCES_GENERAL_BARS_DURATION =
    // "GeneralBarsDuration";
    // public static final String PREFERENCES_GENERAL_BUBBLE_POSITION =
    // "GeneralBubblePosition";
    // public static final String PREFERENCES_SHOW_FULL_SCREEN =
    // "GeneralFullScreen";
    // public static final String PREFERENCES_GENERAL_HIDE_TITLE_BARS =
    // "GeneralHideTitleBars";
    // public static final String PREFERENCES_SHOW_TOAST_ON_TAB_SWITCH =
    // "GeneralShowToastOnTabSwitch";

    public static final String PREFERENCES_DOWNLOAD_DIR = "DownloadDir";

    private static SharedPreferences settings;

    public static SharedPreferences getSharedPreferenceSettings() {
        return settings;
    }

    /**
     * Initialize the search url "constants", which depends on the user local.
     *
     * @param context The current context.
     */
    public static void initialize(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static String getDefaultDownloadPath() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                String defaultDownloadPath = (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Florist")).getAbsolutePath();
                return defaultDownloadPath;
            }
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getDownloadDirPath() {
        try {
            if (!getDefaultDownloadPath().equals("")) {
                File f = new File(settings.getString(PREFERENCES_DOWNLOAD_DIR, getDefaultDownloadPath()));
                if (!f.exists()) {
                    f.mkdirs();
                }
                return f.getAbsolutePath();
            } else {
                return "";
            }
        } catch (Exception ex) {
            return "";
        }
    }

    private static void setSystemPropertyDownloadPath(String path) {
        System.setProperty("DOWNLOAD_PATH", path);
    }

    public static void setDowloadPath(String dirPath) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFERENCES_DOWNLOAD_DIR, dirPath);
        editor.commit();
        setSystemPropertyDownloadPath(dirPath);
    }
}
