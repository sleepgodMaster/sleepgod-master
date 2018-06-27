package com.sleepgod.permission;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cool on 2018/6/25.
 * /**
 * 获取全局的context，也就是Application Context
 *
 * @return 返回全局的Context，只适用于android 4.0以后
 */

@TargetApi(14)
public class Utils {

    public static Context getContext() {
        Application application = null;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = activityThread.getMethod("currentApplication");
            application = (Application) currentApplicationMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return application;
    }

    /**
     * Get a list of permissions in the manifest.
     */
    public static List<String> getManifestPermissions(Context context) {
        context = context.getApplicationContext();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null || permissions.length == 0) {
                throw new IllegalStateException("You did not register any permissions in the manifest.xml.");
            }
            return Collections.unmodifiableList(Arrays.asList(permissions));
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package name cannot be found.");
        }
    }


    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    /**
     * px转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / density + 0.5f);
        return dp;
    }
}