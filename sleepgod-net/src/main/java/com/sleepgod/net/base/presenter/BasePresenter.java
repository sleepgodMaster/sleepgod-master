package com.sleepgod.net.base.presenter;

import android.app.Activity;

import com.sleepgod.net.base.view.BaseView;

public abstract class BasePresenter<V extends BaseView> {
    public V mView;
    public Activity mActivity;

    public void onCreate(V view, Activity activity) {
        this.mView =  view;
        this.mActivity = activity;
    }

    public void onDestroy() {
        mView = null;
        mActivity = null;
    }

    public void onLowMemory() {
        mView = null;
        mActivity = null;
    }
}
