package com.sleepgod.sleepgod.presenter;


import android.util.Log;

import com.sleepgod.net.HttpClient;
import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.http.Callback;
import com.sleepgod.sleepgod.bean.TestBean;
import com.sleepgod.sleepgod.view.MainView;

import java.util.HashMap;

public class MainPresenter extends BasePresenter<MainView> {

    public void login() {
        HashMap<String, Object> params = new HashMap<>();
        HttpClient.create(this)
                .params(params)
                .get()
                .showLoddingDialog(true)
                .url("api/data/Android/10/1")
                .builder()
                .callback(new Callback<TestBean>() {
                    @Override
                    public void onSuccess(TestBean testBean) {
                        Log.e("399",testBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("399",msg);
                    }
                });

    }
}
