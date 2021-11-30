package com.greener;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//자동로그인시 필요 : 정보를 저장하기위한 메소드
public class SaveSharedPreference {

    static String PREF_USER_NAME = "username"; //key값


    //모든 액티비티에서 인스턴스를 얻기위한 메소드
    static SharedPreferences getSharedPreferences(Context ctx) {
        //return PreferenceManager.getDefaultSharedPreferences(ctx);
        return ctx.getSharedPreferences(PREF_USER_NAME,Context.MODE_PRIVATE);

    }

    // 계정 정보 저장. 로그인시 자동로그인 여부에 따라 호출 될 메소드. username이 저장
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    // 저장된 정보 가져오기.
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data editor.commit();
    }
}
