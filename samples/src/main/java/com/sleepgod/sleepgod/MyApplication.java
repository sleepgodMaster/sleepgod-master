package com.sleepgod.sleepgod;

import android.app.Application;

import com.sleepgod.ok.util.AppLog;

/**
 * Created by cuiweicai on 2018/6/14.
 */

public class MyApplication extends Application  {
    public static MyApplication mApplication;

    public static MyApplication getApplication() {
        return mApplication;
    }

    @Override public void onCreate() {
        super.onCreate();
        AppLog.e("MyApplication Created..........");
        mApplication = this;
        AppLog.setDEBUG(true);
    }

}
