package com.sleepgod.net.base.activity;

import android.os.Bundle;

import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.base.view.BaseView;

public abstract class MVPActivity<V extends BaseView,P extends BasePresenter<V>> extends BaseActivity {

    public P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        if (mPresenter != null) {
            mPresenter.onCreate((V) this, this);
        }
        super.onCreate(savedInstanceState);
    }

    protected abstract P onCreatePresenter();

    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        if(mPresenter != null){
            mPresenter.onLowMemory();
        }
        super.onLowMemory();
    }
}
