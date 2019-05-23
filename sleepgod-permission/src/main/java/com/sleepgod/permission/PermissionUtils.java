package com.sleepgod.permission;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.sleepgod.permission.permission.PermissionResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by cool on 2018/6/25.
 * /**
 * 获取全局的context，也就是Application Context
 */

public class PermissionUtils {

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
     *
     * @param context context
     * @return list
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

    public static PermissionResult checkPermissions(Context context, String[] permissions) {
        checkPermissionsInManifest(context, permissions);
        PermissionResult permissionResult = new PermissionResult();
        List<String> grantedList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                grantedList.add(permission);
            } else {
                deniedList.add(permission);
            }
        }
        permissionResult.grantedList = grantedList;
        permissionResult.deniedList = deniedList;

        if (grantedList.size() == permissions.length) {
            permissionResult.hasPermission = true;
        }
        return permissionResult;
    }

    private static void checkPermissionsInManifest(Context context, String[] permissions) {
        List<String> manifestPermissions = PermissionUtils.getManifestPermissions(context);
        for (String p : permissions) {
            if (!manifestPermissions.contains(p)) {
                throw new IllegalStateException(String.format("The permission %1$s is not registered in manifest.xml", p));
            }
        }
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dp      dp
     * @return px
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    /**
     * px转dp
     *
     * @param context context
     * @param px      px
     * @return dp
     */
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / density + 0.5f);
        return dp;
    }
}
