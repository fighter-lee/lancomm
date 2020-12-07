package com.fighter.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取APP、Android系统、硬件设备的基本信息
 * Created by fighter_lee on 19/07/17.
 */
public class AppInfoUtil {

    public static int getVersionCode() {
        PackageInfo info;
        Context context = ContextVal.getContext();
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName() {
        PackageInfo info;
        Context context = ContextVal.getContext();
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPackageName() {
        Context context = ContextVal.getContext();
        return context.getPackageName();
    }

    public static String getAppSignature() {
        Context context = ContextVal.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return packageInfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
