package com.sleepgod.net.base.presenter;

import android.app.Activity;

import com.sleepgod.net.base.view.BaseView;
import com.sleepgod.net.http.Callback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
/**
 * Created by cool on 2018/6/20.
 */
public abstract class BasePresenter<V extends BaseView> {
    public V mView;
    public Activity mActivity;
    private final List<Disposable> mDisposableList = new ArrayList<>();

    public void onCreate(V view, Activity activity) {
        this.mView = view;
        this.mActivity = activity;
    }

    public void onDestroy() {
        release();
    }

    public void onLowMemory() {
        release();
    }

    private void release() {
        mView = null;
        mActivity = null;
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mDisposableList.clear();
    }

    public void addDisposable(Disposable d) {
        if (!mDisposableList.contains(d))
            mDisposableList.add(d);
    }
}
