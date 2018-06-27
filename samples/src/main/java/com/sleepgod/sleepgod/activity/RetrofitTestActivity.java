package com.sleepgod.sleepgod.activity;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.org.sleepgod.widget.ColorfulProgressBar;
import com.sleepgod.net.base.activity.MVPActivity;
import com.sleepgod.net.http.Progress;
import com.sleepgod.permission.annotation.APermission;
import com.sleepgod.sleepgod.R;
import com.sleepgod.sleepgod.presenter.MainPresenter;
import com.sleepgod.sleepgod.view.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RetrofitTestActivity extends MVPActivity<MainView,MainPresenter> implements MainView{

    @BindView(R.id.colorful_progressbar)
    ColorfulProgressBar mColorfulProgressBar;
    @BindView(R.id.tv_text)
    TextView mTextView;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_retrofit_test;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }

    public void testGet(View view) {
        mPresenter.testGet();
    }

    public void testPost(View view){
        mPresenter.testPost();
    }
    public void testPostJson(View view){
        mPresenter.testPostJson();
    }

    @APermission(permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void testDownload(View view){
        mPresenter.testDownLoad();
    }
    public void testUpload(View view){
        mPresenter.testUpload();
    }

    @Override
    public void onProgress(Progress progress) {
        mColorfulProgressBar.setMax(progress.totalSize);
        mColorfulProgressBar.setProgress(progress.currentSize);
    }

    @Override
    public void getDataSuccess(String data) {
        mTextView.setText(data);
    }

    @Override
    public void getDataFail(String msg) {
        mTextView.setText(msg);
    }
}
