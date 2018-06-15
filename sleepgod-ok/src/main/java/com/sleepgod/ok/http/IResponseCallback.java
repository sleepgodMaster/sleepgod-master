package com.sleepgod.ok.http;


/**
 * 获取网络数据的回调接口
 */
public interface IResponseCallback {


    void onStart();

    void onFinish(String response);

    void onError(Exception e);



}
