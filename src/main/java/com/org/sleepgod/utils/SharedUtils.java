package com.org.sleepgod.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by jimda on 2016/1/21.
 */
public class SharedUtils {

    /**
     * 存放boolean值
     *
     * @param key   键
     * @param value 值
     */
    public static void putBoolean(Context context, String key , boolean value) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).commit();
    }

    /**
     * 获取boolean值
     *
     * @param key 键
     * @return boolean值
     */
    public static boolean getBoolean(Context context,String key , boolean defaultValue) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    /**
     * 存放字符串
     * @param key 键
     * @param value 值
     */
    public static void putString(Context context,String key , String value) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).commit();
    }

    /**
     * 获取字符串
     * @param key 键
     * @param defaultValue 默认值
     * @return 返回一个字符串
     */
    public static  String getString(Context context ,String key ,String defaultValue){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultValue);
    }

    /**
     * 存放浮点型数据
     * @param key 键
     * @param value 值
     */
    public static void putFloat(Context context,String key , float value){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(key,value).commit();
    }

    /**
     * 获取浮点型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 返回一个浮点型数据
     */
    public static float getFloat(Context context,String key,float defaultValue){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key,defaultValue);
    }

    /**
     * 存放整型
     * @param key 键
     * @param value 值
     */
    public static void putInt(Context context,String key,int value){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,value).commit();
    }

    /**
     * 获取整型
     * @param key 键
     * @param defaultValue 默认值
     * @return 返回以一个整型
     */
    public static int getInt(Context context,String key,int defaultValue){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,defaultValue);
    }

    /**
     * 存放长整型
     * @param key
     * @param value
     */
    public static void putLong(Context context,String key,long value){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key,value).commit();
    }

    /**
     * 获取长整型
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context,String key,long defaultValue){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,defaultValue);
    }

    /**
     * 存放一个Set的String集合
     * @param key
     * @param values
     */
    public static void putStringSet(Context context,String key,Set<String> values){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet(key,values).commit();
    }

    /**
     * 获取一个Set<String>集合
     * @param key
     * @param defaultVaues
     * @return Set<String>
     */
    public static Set<String> getStringSet(Context context,String key,Set<String> defaultVaues){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key,defaultVaues);
    }
}