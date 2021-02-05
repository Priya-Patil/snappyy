package com.m90.zero.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    public static int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "badsha";
    // All Shared Preferences Keys
    private static final String Mobile = "mobile";
    private static final String Cityid = "cityid";
    private static final String faqOnOff = "faqOnOff";
    private static final String Userid = "Userid";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String latPickup = "latPickup";
    private static final String longPickup = "longPickup";
    private static final String latDrop = "latDrop";
    private static final String longDrop = "longDrop";
    private static final String pickup = "pickup";
    private static final String drop = "drop";
    private static final String pnumber = "pnumber";
    private static final String dnumber = "dnumber";
    private static final String pname = "pname";
    private static final String dname = "dname";
    private static final String ldrop = "ldrop";
    private static final String lpick = "lpick";
    private static final String litem = "litem";
    private static final String pickdropfrmshop = "pickdropfrmshop";
    private static final String buyanything = "buyanything";
    private static final String sendrecvbuy = "sendrecvbuy";
    private static final String amt = "amt";
    private static final String loginType = "loginType";
    private static final String weightCategoryType = "weightCategoryType";

    private static final String kmAmount = "kmAmount";
    private static final String km = "km";
    private static final String regularAmount = "regularAmount";
    private static final String ischeck = "ischeck";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //
    public void setMobile(String S) {
        editor.putString(Mobile, S);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(Mobile, null);
    }

     public void setLpick(String S) {
        editor.putString(lpick, S);
        editor.commit();
    }

    public String getLpick() {
        return pref.getString(lpick, null);
    }

      public void setLoginType(String S) {
        editor.putString(loginType, S);
        editor.commit();
    }

    public String getLoginType() {
        return pref.getString(loginType, null);
    }

      public void setLdrop(String S) {
        editor.putString(ldrop, S);
        editor.commit();
    }

    public String getLdrop() {
        return pref.getString(ldrop, null);
    }

        public void setLitem(String S) {
        editor.putString(litem, S);
        editor.commit();
    }

    public String getLitem() {
        return pref.getString(litem, null);
    }

     public void setPmobile(String S) {
        editor.putString(pnumber, S);
        editor.commit();
    }

    public String getPmobile() {
        return pref.getString(pnumber, null);
    }

     public void setDmobile(String S) {
        editor.putString(dnumber, S);
        editor.commit();
    }

    public String getDmobile() {
        return pref.getString(dnumber, null);
    }

     public void setPname(String S) {
        editor.putString(pname, S);
        editor.commit();
    }

    public String getPname() {
        return pref.getString(pname, null);
    }

    public void setDname(String S) {
        editor.putString(dname, S);
        editor.commit();
    }

    public String getDname() {
        return pref.getString(dname, null);
    }



    public void setUserid(String S) {
        editor.putString(Userid, S);
        editor.commit();
    }

    public String getUserid() {
        return pref.getString(Userid, null);
    }

    //
    public void setCityide(String S) {
        editor.putString(Cityid, S);
        editor.commit();
    }

    public String getCityid() {
        return pref.getString(Cityid, null);
    }


    public void setFaqOnOff(String S) {
        editor.putString(faqOnOff, S);
        editor.commit();
    }

    public String getFaqOnOff() {
        return pref.getString(faqOnOff, null);
    }

     public void setLatPickup(String S) {
        editor.putString(latPickup, S);
        editor.commit();
    }

    public String getLatPickup() {
        return pref.getString(latPickup, null);
    }

    public void setLongPickup(String S) {
        editor.putString(longPickup, S);
        editor.commit();
    }

    public String getLongPickup() {
        return pref.getString(longPickup, null);
    }

    public void setPickup(String S) {
        editor.putString(pickup, S);
        editor.commit();
    }

    public String getPickup() {
        return pref.getString(pickup, null);
    }


    public void setDrop(String S) {
        editor.putString(drop, S);
        editor.commit();
    }

    public String getDrop() {
        return pref.getString(drop, null);
    }


    public void setLatDrop(String S) {
        editor.putString(latDrop, S);
        editor.commit();
    }

    public String getLatDrop() {
        return pref.getString(latDrop, null);
    }

    public void setLongDrop(String S) {
        editor.putString(longDrop, S);
        editor.commit();
    }

    public String getLongDrop() {
        return pref.getString(longDrop, null);
    }

    public String getRequestType() {
        return pref.getString(pickdropfrmshop, null);
    }

    public String getBuyanything() {
        return pref.getString(buyanything, null);

    }

    public String getSendrecvbuy() {
        return pref.getString(sendrecvbuy, null);

    }

     public void setRequestType(String S) {
         editor.putString(pickdropfrmshop, S);
         editor.commit();

    }

    public void setBuyanything(String S) {
        editor.putString(buyanything, S);
        editor.commit();
    }

    public  void setSendrecvbuy(String S) {
        editor.putString(sendrecvbuy, S);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setKmAmount(int S) {
        editor.putInt(kmAmount, S);
        editor.commit();
    }

    public int getKmAmount() {
        return pref.getInt(kmAmount, 0);
    }


     public void setWeightCategoryType(int S) {
        editor.putInt(weightCategoryType, S);
        editor.commit();
    }

    public int getWeightCategoryType() {
        return pref.getInt(weightCategoryType,1);
    }

    public void setKm(int S) {
        editor.putInt(km, S);
        editor.commit();
    }

    public int getKm() {
        return pref.getInt(km,0);
    }

    public void setRegularAmount(int S) {
        editor.putInt(regularAmount, S);
        editor.commit();
    }

    public int getRegularAmount() {
        return pref.getInt(regularAmount,0);
    }


    public void setischeck(int S) {
        editor.putInt(ischeck, S);
        editor.commit();
    }

    public int getischeck() {
        return pref.getInt(ischeck,0);
    }



}