package com.fighter.common;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 获取/system/build.prop文件中的属性
 * Created by fighter_lee on 19/11/27.
 */
@SuppressLint({"PrivateApi"})
public class SysPropUtil {

    public static String get(String key) {
        String byReflect = getByReflect(key);
        return byReflect != null ? byReflect : getByShell(key);
    }


    /**
     * 常用的物联网key:
     * ro.fota.version
     * ro.fota.oem
     * ro.fota.device
     * ro.fota.platform
     * ro.fota.type
     *
     * @param key ro.build.version.sdk等
     * @return null if throw exception
     */
    public static String getByReflect(String key) {
        try {
            ClassLoader cl = ContextVal.getContext().getClassLoader();
            Class systemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = systemProperties.getMethod("get", paramTypes);
            return (String) get.invoke(systemProperties, new Object[]{key});
        } catch (Exception e) {
            Trace.e("SysPropUtil", "getByReflect() e = " + e);
            return null;
        }
    }

    public static String getByShell(String key) {
        try {
            Process process = Runtime.getRuntime().exec("getprop " + key);
            process.waitFor();
            BufferedReader successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            // 返回第一行内容
            return successResult.readLine();
        } catch (Exception e) {
            Trace.e("SysPropUtil", "getByShell() e = " + e);
            return null;
        }
    }
}