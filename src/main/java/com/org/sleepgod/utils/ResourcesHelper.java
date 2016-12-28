package com.org.sleepgod.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 工具类，通过各种的资源的名称来获取资源对应的id
 * Created by cuiweicai on 2016/12/20.
 */

public class ResourcesHelper {


    /**
     * 获取layout 的ID
     *
     * @param context
     * @param resourceName
     * @return
     */
    public static int getLayoutId(Context context, String resourceName) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(resourceName, "layout", context.getPackageName());
        // int resId = resources.getIdentifier(getPackageName() + ":layout/" + resourceName, null, null);此方法也可以用
        return resId;
    }


    /**
     * 通过id的名字找到id，例如在TextView中定义的tv_text
      * @param context
     * @param idName
     * @return
     */
    public static int getViewId(Context context, String idName) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(idName, "id", context.getPackageName());
        return resId;

    }

    /**
     * 通过图片的名字来获取对图片id
     * @param context
     * @param drawableName
     * @return
     */
    public static int getDrawableId(Context context, String drawableName){
        Resources resources = context.getResources();
        int resId =resources .getIdentifier(drawableName,"drawable", context.getPackageName());
        return resId;
    }


    /**
     * 通过字符串名字获取id
     * @param context
     * @param stringName
     * @return
     */
    public static int getStringId(Context context,String stringName){
        Resources resources = context.getResources();
        int resId =resources.getIdentifier(stringName,"string", context.getPackageName());
        return resId;
    }

}
