package com.sleepgod.sleepgod.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cool.butterknife.annoation.BindView;
import com.cool.butterknife.core.ButterKnife;
import com.sleepgod.net.base.activity.MVPActivity;
import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.base.view.BaseView;
import com.sleepgod.sleepgod.R;

/**
 * Created by cool on 2018/7/9.
 */
public abstract class BaseSleepActivity<V extends BaseView,P extends BasePresenter<V>> extends MVPActivity<V,P> {

    //测试base类中使用ButterKnife
    @BindView(R.id.tv_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
