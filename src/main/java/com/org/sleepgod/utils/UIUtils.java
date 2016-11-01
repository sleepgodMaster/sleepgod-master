package com.org.sleepgod.utils;

import android.content.Context;

import java.util.Random;

/**
 * Created by cool on 2016/9/14.
 */
public class UIUtils {

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

    /**
     * sp转px
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * 产生随机颜色
     * @return
     */
    public static int randomColor(){
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }

}
