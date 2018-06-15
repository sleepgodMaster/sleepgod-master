package com.sleepgod.ok.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络状态工具类
 */
public class NetUtil {

    /**
     * 无网络
     */
    public static final String TYPE_NONE = "NONE";

    /**
     * WIFI
     */
    public static final String TYPE_WIFI = "WIFI";

    /**
     * 2G
     */
    public static final String TYPE_2G = "2G";

    /**
     * 3G
     */
    public static final String TYPE_3G = "3G";

    /**
     * 4G
     */
    public static final String TYPE_4G = "4G";

    /**
     * 未知网络
     */
    public static final String TYPE_UNKNOWN = "UNKNOWN";

    /**
     * 获取网络详情对象
     */
    private static NetworkInfo getNetWorkInfo(Context context) {
        NetworkInfo activeNetInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            return activeNetInfo;
        } else {
            return null;
        }
    }

    /**
     * 获取网络类型
     */
    public static String getNetWorkType(Context context) {
        String netWorkType = null;

        NetworkInfo networkInfo = getNetWorkInfo(context);

        if (networkInfo == null) {
            return TYPE_NONE;
        } else {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return TYPE_WIFI;
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return TYPE_4G;
                    default:
                        return TYPE_UNKNOWN;
                }
            }
        }
    }

    /**
     * 是否是移动网络
     */
    public static boolean isMobile(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);

        if (networkInfo != null) {
            if (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否是WIFI
     */
    public static boolean isWifi(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

    /**
     * 网络是否可用
     */
    public static boolean isNetAvailable(Context context) {
        return getNetWorkInfo(context) != null;
    }


}

