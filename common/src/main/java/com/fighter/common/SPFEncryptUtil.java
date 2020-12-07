package com.fighter.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.fighter.common.interfaces.ICipher;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 19/07/17.
 */
public class SPFEncryptUtil {

    private static String s_sp_file_name; // sp文件名
    private static ICipher mCipher;

    static {
        s_sp_file_name = AppInfoUtil.getPackageName();
        mCipher = new XorCipherImpl();
    }

    public static void setCipher(@NonNull ICipher cipher) {
        SPFEncryptUtil.mCipher = cipher;
    }

    public static void putInt(@NonNull String key, int value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String enStr = mCipher.encrypt(String.valueOf(value));
        editor.putString(key, enStr);
        editor.apply();
    }

    public static int getInt(@NonNull String key) {
        return getInt(key, -1);
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String enStr = sharedPreferences.getString(key, "");
            if (enStr.length() != 0) {
                String decrypt = mCipher.decrypt(enStr);
                try {
                    return Integer.parseInt(decrypt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultValue;
    }

    public static void putBoolean(@NonNull String key, boolean value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String enStr = mCipher.encrypt(String.valueOf(value));
        editor.putString(key, enStr);
        editor.apply();
    }

    public static boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String enStr = sharedPreferences.getString(key, "");
            if (enStr.length() != 0) {
                String decrypt = mCipher.decrypt(enStr);
                if (decrypt.equalsIgnoreCase("true")) {
                    return true;
                } else if (decrypt.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    public static void putLong(@NonNull String key, long value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String enStr = mCipher.encrypt(String.valueOf(value));
        editor.putString(key, enStr);
        editor.apply();
    }

    public static long getLong(@NonNull String key) {
        return getLong(key, -1);
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String enStr = sharedPreferences.getString(key, "");
            if (enStr.length() != 0) {
                String decrypt = mCipher.decrypt(enStr);
                try {
                    return Long.parseLong(decrypt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultValue;
    }

    public static void putFloat(@NonNull String key, float value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String enStr = mCipher.encrypt(String.valueOf(value));
        editor.putString(key, enStr);
        editor.apply();
    }

    public static float getFloat(@NonNull String key) {
        return getFloat(key, -1f);
    }

    public static float getFloat(@NonNull String key, float defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String enStr = sharedPreferences.getString(key, "");
            if (enStr.length() != 0) {
                String decrypt = mCipher.decrypt(enStr);
                try {
                    return Float.parseFloat(decrypt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultValue;
    }

    public static void putString(@NonNull String key, @NonNull String value) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, mCipher.encrypt(value));
        editor.apply();
    }

    @NonNull
    public static String getString(@NonNull String key) {
        return getString(key, "");
    }

    @NonNull
    public static String getString(@NonNull String key, @NonNull String defaultValue) {
        SharedPreferences sharedPreferences = ContextVal.getContext().getSharedPreferences(
                s_sp_file_name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String enStr = sharedPreferences.getString(key, "");
            if (enStr.length() != 0) {
                String decrypt = mCipher.decrypt(enStr);
                return decrypt;
            }
        }
        return defaultValue;
    }
}
