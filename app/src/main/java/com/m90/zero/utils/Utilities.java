package com.m90.zero.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    public static final String SHARED_PREF = "session";

    public static void saveUserData(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSavedUserData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getString(key, "");

    }

    public static void launchActivity(Activity activity, Class<?> mClass, boolean shouldFinishParent, Bundle bundle) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        if (shouldFinishParent) {
            activity.finish();
        }
    }


    public static boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;

        // return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static void launchActivityForResult(Activity activity, Class<?> mClass, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityWithContext(Context context, Class<?> mClass) {
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
    }

    public static void launchActivityForResult(Activity activity, Class<?> mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResultFromFragment(Fragment fragment, Activity activity, Class<?> mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchActivity(Activity activity, Class<?> mClass, boolean shouldFinishParent) {
        Intent intent = new Intent(activity, mClass);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);

        if (shouldFinishParent) {
            activity.finish();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void finishWithResult(Activity activity, Bundle bundle, int result) {
        Intent i = new Intent();
        if (bundle != null) {
            i.putExtras(bundle);
        }
        activity.setResult(result, i);
        activity.finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the cu rrently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        view = null;
    }

    public static String getDeviceToken(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean emailValidate(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean numberValidation(String number) {
        return Pattern.compile("^[0-9]+$").matcher(number).matches();
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static String getDeviceName() {
        return Build.MANUFACTURER + " - " + Build.MODEL;
    }

    public static void showErrorMessage(Activity activity, String msg) {
        try {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Log.e(TAG, "showErrorMessage: ", ex);
        }
    }

    public static void showErrorMessage(Activity activity, String msg, int length) {
        try {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), msg, length).show();
        }
        catch (Exception ex){
            Log.e(TAG, "showErrorMessage: ", ex);
        }
    }

    public static String getCurrentDateTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public static String getCurrentDate(){
       // SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.US);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        return filename;
    }



//    public static void sendIntent(Context context, String action) {
//        final Intent intent = new Intent(context, MusicService.class);
//        intent.setAction(action);
//        context.startService(intent);
//    }
//
//    public static void sendIntent(Context context, String action, Bundle b) {
//        final Intent intent = new Intent(context, MusicService.class);
//        intent.putExtras(b);
//        intent.setAction(action);
//        context.startService(intent);
//    }

      public static String getCurrentMonthFirstDate(){
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //System.out.println(df.format(c.getTime()));
    return df.format(c.getTime());
}



    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }
   /*

       convert string to date in DDMMYYYY format

       String date=item.getStartdate();
        SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Log.e("Adapter", date);
        try {
            Date d=ddMMyyyyFormat.parse(date);
            System.out.println("DATE"+d);
            System.out.println("Formated"+ddMMyyyyFormat.format(d));
            Log.e("Adapter", "sss  "+String.valueOf(ddMMyyyyFormat.format(d)));
        }
        catch(Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep"+e);
        }*/


}
