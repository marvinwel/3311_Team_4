package com.example.password_saver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    static void saveStringInSp(Context ctx, String key, String value)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

    static String getStringFromSp(Context ctx, String key)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }
}
