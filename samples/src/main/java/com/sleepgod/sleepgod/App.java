package com.sleepgod.sleepgod;

import android.app.Application;

import com.sleepgod.ok.util.AppLog;

/**
 * Created by cuiweicai on 2018/6/14.
 */

public class App extends Application  {
    public static App mApp;

    public static App getApplication() {
        return mApp;
    }

    @Override public void onCreate() {
        super.onCreate();
        mApp = this;
        AppLog.setDEBUG(true);
    }

}
