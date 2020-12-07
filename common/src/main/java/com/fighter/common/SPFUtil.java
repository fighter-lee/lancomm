package com.fighter.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 19/07/17.
 */
public class SPFUtil {

    private static String s_sp_file_name; // sp文件名

    static {
        s_sp_file_name = AppInfoUtil.getPackageName();
    }


    public static void putInt(@NonNull String key, int value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(@NonNull String key) {
        return getInt(key, -1);
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putBoolean(@NonNull String key, boolean value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putLong(@NonNull String key, long value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(@NonNull String key) {
        return getLong(key, -1);
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }


    public static void putFloat(@NonNull String key, float value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(@NonNull String key) {
        return getFloat(key, -1f);
    }

    public static float getFloat(@NonNull String key, float defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void putString(@NonNull String key, @NonNull String value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(@NonNull String key) {
        return getString(key, "");
    }
    
    @NonNull
    public static String getString(@NonNull String key, @NonNull String defValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }
}
