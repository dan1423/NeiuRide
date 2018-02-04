package com.app.danny.neiuber.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

/**
 * Created by danny on 1/20/18.
 * This class is responsible for keeping user logged in, util he logs out
 * it does so by saving user info and credentials after user logs in, till he he decides to log out
 */

public class SavedUserInfo {

    static final String USER_INFO= "user_Info";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserInfo(Context ctx,String userInfo) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_INFO, userInfo);
        editor.commit();
    }

    public static String getUserInfo (Context ctx) {
        return getSharedPreferences(ctx).getString(USER_INFO, "");
    }

    public static void clearAllSharedPreferences(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
