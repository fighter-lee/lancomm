package com.fighter.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * //TODO 是否需要设置ping超时
 * Created by fighter_lee on 19/08/22.
 */
public class NetworkUtil {
    private static final String TAG = "NetUtils";

    public static final String NETWORK_WIFI = "WiFi";
    public static final String NETWORK_2G = "2G";
    public static final String NETWORK_3G = "3G";
    public static final String NETWORK_4G = "4G";
    // 没有网络连接
    public static final String NETWORK_OTHER = "Other";

    /**
     * 通过系统接口和ping一起判断
     *
     * @return true if available
     */
    public static boolean isNetWorkAvailable() {
        return isAvailableBySystem() || isAvailableByPing("");
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>需要异步 ping，如果 ping 不通就说明网络不可用</p>
     *
     * @param ip ip 地址（自己服务器 ip），如果为空，ip 为阿里巴巴公共 ip
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    private static boolean isAvailableByPing(String ip) {
        if (TextUtils.isEmpty(ip)) {
            ip = "223.5.5.5";// 阿里巴巴公共 ip
        }
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 -w 1 %s", ip), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            Trace.v("NetworkUtils", "isAvailableByPing() called " + result.errorMsg);
        }
        if (result.successMsg != null) {
            Trace.v("NetworkUtils", "isAvailableByPing() called " + result.successMsg);
        }
        return ret;
    }

    private static boolean isAvailableBySystem() {
        boolean ret = false;
        try {
            ConnectivityManager connectManager = (ConnectivityManager) ContextVal.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectManager == null) {
                return ret;
            }
            NetworkInfo[] infos = connectManager.getAllNetworkInfo();
            if (infos == null) {
                return ret;
            }
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if (infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * WIFI是否连接
     *
     * @return true 代表是WIFI上网
     */
    private static boolean isWifiConnected() {
        return NETWORK_WIFI.equals(getNetworkState());
    }

    /**
     * 获取当前网络连接类型
     */
    public static String getNetworkState() {
        Context context = ContextVal.getContext();
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //如果当前没有网络
        if (null == connManager) {
            return NETWORK_OTHER;
        }

        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_OTHER;
        }

        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORK_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName) || "WCDMA".equalsIgnoreCase(strSubTypeName) || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                                return NETWORK_3G;
                            } else {
                                Trace.d(TAG, "Don't know the type of network but can go online");
                                return NETWORK_4G;
                            }
                    }
                }
            }
        }
        return NETWORK_OTHER;
    }
}
