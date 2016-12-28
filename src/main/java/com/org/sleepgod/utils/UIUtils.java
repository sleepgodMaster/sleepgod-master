package com.org.sleepgod.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


    /**
     * 格式化时间
     * @return 如果是今天，返回：13:24 如果是昨天，返回： 昨天13:24 当月返回 11-7 13:24 其他返回 2016-10-2
     */
    public static String formatDate(Date date) {
        Date currentDate = new Date();
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set( Calendar.HOUR_OF_DAY, 0);
        yesterday.set( Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(current.after(today)){//今天
            format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        }else if(current.before(today) && current.after(yesterday)){//昨天
            format = new SimpleDateFormat("HH:mm");
            return "昨天 "+format.format(date);
        }else if(date.getYear()==currentDate.getYear()&& date.getMonth()==currentDate.getMonth()){
            format = new SimpleDateFormat("MM-dd HH:mm");
            return format.format(date);
        }else {
            return format.format(date);
        }

    }

}
