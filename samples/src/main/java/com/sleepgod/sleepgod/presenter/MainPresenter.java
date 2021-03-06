package com.sleepgod.sleepgod.presenter;


import android.util.Log;

import com.google.gson.Gson;
import com.sleepgod.net.callback.FileCallback;
import com.sleepgod.net.callback.HttpCallback;
import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.http.HttpClient;
import com.sleepgod.net.http.Progress;
import com.sleepgod.sleepgod.bean.BaseBean;
import com.sleepgod.sleepgod.bean.NewsBean;
import com.sleepgod.sleepgod.bean.RequestBean;
import com.sleepgod.sleepgod.bean.WeatherBean;
import com.sleepgod.sleepgod.view.MainView;

import java.util.HashMap;

/**
 * Created by cool on 2018/6/20.
 */
public class MainPresenter extends BasePresenter<MainView> {

    public void testGet() {
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("appKey","1328ffff76a74d2987914a0de08b9f44");
//        params.put("category","要闻");
//        params.put("updateTime","2018-06-21 00:00:00");
        HttpClient.create(this)
//                .params(params)
                .requestParams("appKey", "1328ffff76a74d2987914a0de08b9f44")
                .requestParams("category", "要闻")
                .requestParams("updateTime", "2018-06-21 00:00:00")
                .heads("a", "aa")
                .showLodding(true)
                .url("api/news/list")
                .builder()
                .execute(new HttpCallback<BaseBean<NewsBean>>() {
                    @Override
                    public void onSuccess(BaseBean<NewsBean> newsBeanBaseBean) {
                        mView.getDataSuccess(newsBeanBaseBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });

    }

    public void testPost() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appKey", "b5baa6d5add44cc3a6f9bd7596953669");
        params.put("area", "苏州");

        HttpClient.create(this)
                .params(params)
                .showLodding(true)
                .url("api/weather/area")
                .heads("a", "aa")
                .heads("b", "bb")
                .post()
                .builder()
                .execute(new HttpCallback<BaseBean<WeatherBean>>() {
                    @Override
                    public void onSuccess(BaseBean<WeatherBean> weatherBeanBaseBean) {
                        mView.getDataSuccess(weatherBeanBaseBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });
    }

    /**
     * FileCallback 有3个重载构造函数
     */
    public void testDownLoad() {
        //http://app.mi.com/download/427632
        HttpClient.create(this)
                .download()
                .url("software/mobile/MockMobile_v3.4.0.0.apk")
                .baseUrl("http://cdn.mockplus.cn/")
                .builder()
                .execute(new FileCallback() {
                    @Override
                    public void onStart() {
                        Log.e("399", "onStart");
                    }

                    @Override
                    public void onDownloadComplete() {
                        Log.e("399", "onDownloadComplete" + " thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("399", "onError" + " thread: " + Thread.currentThread().getName() + "  msg:" + msg);
                    }

                    @Override
                    public void onDownloading(Progress progress) {
                        mView.onProgress(progress);
                    }
                });
    }

    public void testPostJson() {
        RequestBean requestBean = new RequestBean("b5baa6d5add44cc3a6f9bd7596953669", "苏州");
        HttpClient.create(this)
                .params(requestBean)
                .postJson()
                .url("api/weather/area")
                .showLodding(true)
                .builder()
                .execute(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mView.getDataSuccess(s);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });
    }

    public void testUpload() {
//        HttpClient.create(this)
//                .url("")
//                .upload(new File(""))
//                .builder()
//                .execute(new HttpCallback<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//
//                    }
//
//                    @Override
//                    public void onError(String msg) {
//
//                    }
//                });

    }

}
