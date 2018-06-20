package com.sleepgod.sleepgod.presenter;


import android.util.Log;

import com.sleepgod.net.http.HttpClient;
import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.http.Callback;
import com.sleepgod.sleepgod.bean.TestBean;
import com.sleepgod.sleepgod.view.MainView;

import java.util.HashMap;
/**
 * Created by fuwei on 2018/6/20.
 */
public class MainPresenter extends BasePresenter<MainView> {

    public void login() {
        HashMap<String, Object> params = new HashMap<>();
        HttpClient.create(this)
                .params(params)
                .get()
                .showLodding(true)
                .url("api/data/Android/10/1")
                .builder()
                .execute(new Callback<TestBean>() {
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
