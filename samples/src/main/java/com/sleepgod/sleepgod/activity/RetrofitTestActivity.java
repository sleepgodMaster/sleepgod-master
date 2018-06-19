package com.sleepgod.sleepgod.activity;

import android.view.View;

import com.sleepgod.net.base.activity.MVPActivity;
import com.sleepgod.sleepgod.R;
import com.sleepgod.sleepgod.presenter.MainPresenter;
import com.sleepgod.sleepgod.view.MainView;

public class RetrofitTestActivity extends MVPActivity<MainView,MainPresenter> implements MainView{

    @Override
    public int setLayoutResID() {
        return R.layout.activity_retrofit_test;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }

    public void click(View view) {
        mPresenter.login();
    }
}
