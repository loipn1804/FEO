package com.fareastorchid.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fareastorchid.MainActivity;
import com.fareastorchid.model.controller.Controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import br.com.pierry.simpletoast.SimpleToast;

@SuppressWarnings("deprecation")
public class Utils {
    private static int faviconSize = -1;
    private static int imageButtonSize = -1;
    private static int faviconSizeForBookmarks = -1;

    public static int getImageButtonSize(Activity activity) {
        if (imageButtonSize == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    imageButtonSize = 16;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    imageButtonSize = 32;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    imageButtonSize = 48;
                    break;
                default:
                    imageButtonSize = 32;
                    break;
            }
        }

        return imageButtonSize;
    }

    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isConnected = false;

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                isConnected = activeNetwork.isConnected();
            }
            return isConnected;
        } catch (Exception ex) {
            return false;
        }
    }

    // walk around --emulate select event to hide text seletion
    public static void emulateSelectKey() {
        @SuppressWarnings("unused")
        KeyEvent backEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_SELECT, 0, 0);
        // **
        // backEvent.dispatch(Controller.getInstance().getUtilityActivity().getCurrentWebView());
    }

    /**
     * Get the required size of the favicon, depending on current screen
     * density.
     *
     * @param activity The current activity.
     * @return The size of the favicon, in pixels.
     */
    public static int getFaviconSize(Activity activity) {
        if (faviconSize == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    faviconSize = 12;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    faviconSize = 24;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    faviconSize = 32;
                    break;
                default:
                    faviconSize = 24;
                    break;
            }
        }

        return faviconSize;
    }

    /**
     * Get the required size of the favicon, depending on current screen
     * density.
     *
     * @param activity The current activity.
     * @return The size of the favicon, in pixels.
     */
    public static int getFaviconSizeForBookmarks(Activity activity) {
        if (faviconSizeForBookmarks == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    faviconSizeForBookmarks = 12;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    faviconSizeForBookmarks = 16;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    faviconSizeForBookmarks = 24;
                    break;
                default:
                    faviconSizeForBookmarks = 16;
                    break;
            }
        }

        return faviconSizeForBookmarks;
    }

    /**
     * Copy a text to the clipboard.
     *
     * @param context      The current context.
     * @param text         The text to copy.
     * @param toastMessage The message to show in a Toast notification. If empty or null,
     *                     does not display notification.
     */
    public static void copyTextToClipboard(Context context, String text, String toastMessage) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboard.setText(text);

        if ((toastMessage != null) && (toastMessage.length() > 0)) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public static void getTextFromClipboard(Context context, String text, String toastMessage) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboard.setText(text);

        if ((toastMessage != null) && (toastMessage.length() > 0)) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    // /**
    // * Share a page.
    // * @param activity The parent activity.
    // * @param title The page title.
    // * @param url The page url.
    // */
    // public static void sharePage(Activity activity, String title, String url)
    // {
    // Intent shareIntent = new Intent(Intent.ACTION_SEND);
    //
    // shareIntent.setType("text/plain");
    // shareIntent.putExtra(Intent.EXTRA_TEXT, url);
    // shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
    //
    // try {
    // activity.startActivity(Intent.createChooser(shareIntent,
    // activity.getString(R.string.main_share_chooser_title)));
    // } catch(android.content.ActivityNotFoundException ex) {
    // // if no app handles it, do nothing
    // }
    // }
    //
    // /**
    // * Display a standard yes / no dialog.
    // * @param context The current context.
    // * @param icon The dialog icon.
    // * @param title The dialog title.
    // * @param message The dialog message.
    // * @param onYes The dialog listener for the yes button.
    // */
    // public static void showYesNoDialog(Context context, int icon, int title,
    // int message, DialogInterface.OnClickListener onYes) {
    // AlertDialog.Builder builder = new AlertDialog.Builder(context);
    // builder.setCancelable(true);
    // builder.setIcon(icon);
    // builder.setTitle(context.getResources().getString(title));
    // builder.setMessage(context.getResources().getString(message));
    //
    // builder.setInverseBackgroundForced(true);
    // builder.setPositiveButton(context.getResources().getString(R.string.commons_yes),
    // onYes);
    // builder.setNegativeButton(context.getResources().getString(R.string.commons_no),
    // new DialogInterface.OnClickListener() {
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // dialog.dismiss();
    // }
    // });
    // AlertDialog alert = builder.create();
    // alert.show();
    // }
    //
    // public static void showPromptDialog(Context context, int icon, int title,
    // View view, DialogInterface.OnClickListener onYes) {
    // AlertDialog.Builder builder = new AlertDialog.Builder(context);
    // builder.setCancelable(true);
    // builder.setIcon(icon);
    // builder.setTitle(context.getResources().getString(title));
    //
    // builder.setView(view);
    //
    // // builder.setInverseBackgroundForced(true);
    // builder.setPositiveButton(context.getResources().getString(R.string.ok),
    // onYes);
    // builder.setNegativeButton(context.getResources().getString(R.string.cancel),
    // new DialogInterface.OnClickListener() {
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // dialog.dismiss();
    // }
    // });
    // AlertDialog alert = builder.create();
    // alert.show();
    // }
    //
    // public static void showToastComingSoon(Context context) {
    // Toast.makeText(context, R.string.commons_coming_soon,
    // Toast.LENGTH_SHORT).show();
    // }

    public static byte[] getEncodedBitmap(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.PNG, 50, os);

        return os.toByteArray();
    }

    public static Rect getRectOnScreen(View view) {
        int[] location = new int[2];
        Rect rect = new Rect();

        view.getLocationOnScreen(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();

        return rect;
    }

    public static String readAssetFile(Context ctx, String fileName) {
        String content = "";
        try {
            InputStream is;
            InputStreamReader isr;
            BufferedReader br;
            is = ctx.getAssets().open(fileName);

            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                content += line;
            }
            is.close();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return content;
    }

    //
    // /**
    // * Check if the SD card is available. Display an alert if not.
    // * @param context The current context.
    // * @param showMessage If true, will display a message for the user.
    // * @return True if the SD card is available, false otherwise.
    // */
    // public static boolean checkCardState(Context context, boolean
    // showMessage) {
    // try{
    // // Check to see if we have an SDCard
    // String status = Environment.getExternalStorageState();
    // if (!status.equals(Environment.MEDIA_MOUNTED)) {
    // int messageId;
    //
    // // Check to see if the SDCard is busy, same as the music app
    // if (status.equals(Environment.MEDIA_SHARED)) {
    // messageId = R.string.commons_sd_card_error_sd_unavailable;
    // } else {
    // messageId = R.string.commons_sd_card_error_no_sd_message;
    // }
    //
    // if (showMessage) {
    // Utils.showErrorDialog(context, R.string.commons_sd_card_error_title,
    // messageId);
    // }
    //
    // return false;
    // }
    //
    // return true;
    // }catch(Exception ex){
    // return false;
    // }
    // }
    //
    // public static boolean sdCardMounted2()
    // {
    // try
    // {
    // if
    // (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    // {
    // return true;
    // }
    // }
    // catch(Exception e){}
    // return false;
    // }
    // /**
    // * Show an error dialog.
    // * @param context The current context.
    // * @param title The title string id.
    // * @param message The message string id.
    // */
    // public static void showErrorDialog(Context context, int title, int
    // message) {
    // try{
    // new AlertDialog.Builder(context)
    // .setTitle(title)
    // .setIcon(android.R.drawable.ic_dialog_alert)
    // .setMessage(message)
    // .setPositiveButton(R.string.ok, null)
    // .show();
    // }catch(Exception ex){}
    // }
    //
    // public static void showErrorDialog(Context context, int title, String
    // message) {
    // try{
    // new AlertDialog.Builder(context)
    // .setTitle(title)
    // .setIcon(android.R.drawable.ic_dialog_alert)
    // .setMessage(message)
    // .setPositiveButton(R.string.ok, null)
    // .show();
    // }
    // catch(Exception ex){}
    // }
    //
    // public static void showInfoDialog(Context context, String message) {
    // try{
    // new AlertDialog.Builder(context)
    // .setMessage(message)
    // .setPositiveButton(R.string.ok, null)
    // .show();
    // }catch(Exception ex){}
    // }
    // public static void showInfoDialog(Context context, int message) {
    // try{
    // new AlertDialog.Builder(context)
    // .setMessage(message)
    // .setPositiveButton(R.string.ok, null)
    // .show();
    // }catch(Exception ex){}
    // }
    //
    // public static String getChangeLog(Context context){
    // try{
    // String changeLogContent = "";
    // InputStream is;
    // InputStreamReader isr;
    // BufferedReader br;
    // String assetName = "change_log";
    // if(Configuration.getUtilityLanguage().equals(LocaleUtils.LANGUAGE_VI)){
    // assetName+="_vi.txt";
    // }
    // else{
    // assetName+=".txt";
    // }
    // is = context.getAssets().open(assetName);
    //
    // isr = new InputStreamReader(is);
    // br = new BufferedReader(isr);
    //
    // String line;
    // while ((line = br.readLine()) != null) {
    // changeLogContent += line;
    // }
    // is.close();
    // return changeLogContent;
    // }
    // catch(Exception e){
    // Log.e("getChangeLog", e.getMessage());
    // return "";
    // }
    // }
    //
    // /**
    // * Display a continue / cancel dialog.
    // * @param context The current context.
    // * @param icon The dialog icon.
    // * @param title The dialog title.
    // * @param message The dialog message.
    // * @param onContinue The dialog listener for the continue button.
    // * @param onCancel The dialog listener for the cancel button.
    // */
    // public static void showContinueCancelDialog(Context context, int icon,
    // String title, String message, DialogInterface.OnClickListener onContinue,
    // DialogInterface.OnClickListener onCancel) {
    // AlertDialog.Builder builder = new AlertDialog.Builder(context);
    // builder.setCancelable(true);
    // builder.setIcon(icon);
    // builder.setTitle(title);
    // builder.setMessage(message);
    //
    // builder.setInverseBackgroundForced(true);
    // builder.setPositiveButton(context.getResources().getString(R.string.commons_ssl_continue),
    // onContinue);
    // builder.setNegativeButton(context.getResources().getString(R.string.commons_ssl_cancel),
    // onCancel);
    // AlertDialog alert = builder.create();
    // alert.show();
    // }
    //
    // public static void createApplicationShortcut(Context context) {
    // ShortcutIconResource icon = ShortcutIconResource.fromContext(context,
    // R.drawable.laban_launcher_opt_1);
    //
    // Intent i = new Intent();
    // i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context,
    // SplashActivity.class));
    // i.putExtra(Intent.EXTRA_SHORTCUT_NAME,
    // context.getString(R.string.app_name));
    // i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
    // i.putExtra("duplicate", false);
    // i.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    //
    // context.sendBroadcast(i);
    // }

    public static void createActivityShortcut(Context context, Class<?> cls, ShortcutIconResource icon, String title) {
        Intent i = new Intent();
        i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context, cls));
        i.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        i.putExtra("duplicate", false);
        i.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        context.sendBroadcast(i);
    }

    /**
     * This method convets dp unit to equivalent device specific value in
     * pixels.
     *
     * @param dp      A value in dp(Device independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent Pixels equivalent to dp according to
     * device
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent db equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void deleteFile(String filename) {
        try {
            File delete = new File(filename);
            delete.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMediaPath(Activity mActivity, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            // cursor = mActivity.managedQuery(uri, projection, null, null,
            // null);
            cursor = mActivity.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.getCount() >= 1) {
                cursor.moveToFirst();
                int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(dataColumnIndex);
                return path;
            }
            return uri.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    public static String parseTypeFile(String strInput) {
        String name = "";
        try {
            name = strInput.substring(strInput.lastIndexOf("/"));
            name = name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
        }

        return name;
    }

    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            if (filename.equalsIgnoreCase("errorpage.html") || filename.equalsIgnoreCase("error_ico.png") || filename.equalsIgnoreCase("refresh.png")) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    ContextWrapper cw = new ContextWrapper(context);
                    // path to /data/data/yourapp/app_data/htmlDir
                    File directory = cw.getDir("htmlDir", Context.MODE_PRIVATE);
                    // Create htmlDir
                    File outFile = new File(directory, filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                }
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void rewrite2File(Context ctx, String file) {
        FileWriter fWriter;
        try {
            ContextWrapper cw = new ContextWrapper(ctx);
            // path to /data/data/yourapp/app_data/htmlDir
            File directory = cw.getDir("htmlDir", Context.MODE_PRIVATE);
            // // Create htmlDir
            // File outFile = new File(directory, filename);
            fWriter = new FileWriter(directory + "/errorpage.html");
            fWriter.write(file);
            fWriter.flush();
            fWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logger(String type, String msg, String tag) {
        if (type.equals("v")) {
            Log.v(tag, msg);
        } else if (type.equals("d")) {
            Log.d(tag, msg);
        } else if (type.equals("i")) {
            Log.i(tag, msg);
        } else if (type.equals("w")) {
            Log.w(tag, msg);
        }

    }

    public static String convertToNonUnicodeString(String str) {
        String[] charA_array = {"Ã ", "Ã¡", "áº¡", "áº£", "Ã£", "Ã¢", "áº§", "áº¥", "áº­", "áº©", "áº«", "Äƒ", "áº±", "áº¯", "áº·", "áº³", "áºµ"};
        String[] charE_array = {"Ãª", "á»�", "áº¿", "á»‡", "á»ƒ", "á»…", "Ã¨", "Ã©", "áº¹", "áº»", "áº½"};
        String[] charI_array = {"Ã¬", "Ã­", "á»‹", "á»‰", "Ä©"};
        String[] charO_array = {"Ã²", "Ã³", "á»�", "á»�", "Ãµ", "Ã´", "á»“", "á»‘", "á»™", "á»•", "á»—", "Æ¡", "á»�", "á»›", "á»£", "á»Ÿ",
                "á»¡"};
        String[] charU_array = {"Ã¹", "Ãº", "á»¥", "á»§", "Å©", "Æ°", "á»«", "á»©", "á»±", "á»­", "á»¯"};
        String[] charY_array = {"á»³", "Ã½", "á»µ", "á»·", "á»¹"};
        String[] charD_array = {"Ä‘"};

        ArrayList<String[]> listCharArray = new ArrayList<String[]>();
        listCharArray.add(charA_array);
        listCharArray.add(charD_array);
        listCharArray.add(charE_array);
        listCharArray.add(charI_array);
        listCharArray.add(charO_array);
        listCharArray.add(charU_array);
        listCharArray.add(charY_array);

        String[] listChar = {"a", "d", "e", "i", "o", "u", "y"};
        String nonUnicodeString = str;

        for (int i = 0; i < listCharArray.size(); i++) {
            for (int j = 0; j < listCharArray.get(i).length; j++) {
                String unicodeCharacter = listCharArray.get(i)[j];
                if (nonUnicodeString.contains(unicodeCharacter)) {
                    nonUnicodeString = nonUnicodeString.replace(unicodeCharacter, listChar[i]);
                }
            }
        }
        return nonUnicodeString;
    }


    /**
     * Check if the SD card is available. Display an alert if not.
     *
     * @param context     The current context.
     * @param showMessage If true, will display a message for the user.
     * @return True if the SD card is available, false otherwise.
     */
    public static boolean checkCardState(Context context, boolean showMessage) {
        try {
            // Check to see if we have an SDCard
            String status = Environment.getExternalStorageState();
            if (!status.equals(Environment.MEDIA_MOUNTED)) {
                String message;

                // Check to see if the SDCard is busy, same as the music app
                if (status.equals(Environment.MEDIA_SHARED)) {
                    message = "SD Card unavailable";
                } else {
                    message = "No SD Card found.";
                }

                if (showMessage) {
                    SimpleToast.error(Controller.getActInstance(MainActivity.class), message);
                }

                return false;
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
