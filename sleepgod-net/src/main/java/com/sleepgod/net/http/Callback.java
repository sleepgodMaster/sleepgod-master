package com.sleepgod.net.http;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sleepgod.net.HttpClient;
import com.sleepgod.net.base.presenter.BasePresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 回调类
 */
public abstract class Callback<ResultType> {
    private boolean showLoddingDialog;
    private String title;
    private WeakReference<BasePresenter> presenterWReference;

    public void init(WeakReference<BasePresenter> presenterWReference, boolean showLoddingDialog, String title) {
        this.presenterWReference = presenterWReference;
        this.showLoddingDialog = showLoddingDialog;
        this.title = title;
    }

    public void onSubscribe(Disposable d) {
        onStart();
    }

    public void onNext(ResponseBody responseBody) {
        parseResponse(responseBody);
    }

    public void onError(Throwable throwable) {
        error(throwable);
    }

    private void onStart() {
        if (showLoddingDialog && presenterWReference != null && presenterWReference.get() != null) {
            presenterWReference.get().mView.showLodingDialog(title);
        }
    }

    public void onComplete() {
        if (showLoddingDialog && presenterWReference != null && presenterWReference.get() != null) {
            presenterWReference.get().mView.hideLodingDialog();
        }
    }

    private void parseResponse(ResponseBody responseBody) {
        if (responseBody == null) {
            onError("客户端异常");
            return;
        }

        Type genType = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        try {
            if (type == ResponseBody.class) {
                onSuccess((ResultType) responseBody);
            } else if (type == String.class) {
                onSuccess((ResultType) responseBody.string());
            } else {
                ResultType resultType = new Gson().fromJson(responseBody.string(), type);
                onSuccess(resultType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void onSuccess(ResultType resultType);

    public abstract void onError(String msg);


    private void error(Throwable throwable) {
        onComplete();
        String errString;
        if (throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
            errString = "链接超时";
        } else if (throwable instanceof SocketException) {
            errString = "请检查网络设置";
        } else if (throwable instanceof SocketTimeoutException) {
            errString = "链接超时";
        } else if (throwable instanceof UnknownHostException) {
            errString = "请检查网络设置";
        } else if (throwable instanceof ArithmeticException) {
            errString = "客户端异常";
        } else if (throwable instanceof NullPointerException) {
            errString = "客户端异常";
        } else if(throwable instanceof JsonParseException){
            errString = "数据解析异常";
        }else if (throwable instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) throwable;
            if (httpEx.getMessage().contains("timed out")) {
                errString = "连接超时";
            } else if (httpEx.code() == 500) {
                errString = "服务君累趴下啦";
            } else if (httpEx.code() == 0) {
                errString = "服务君累趴下啦";
            } else if (httpEx.code() == 403) {
                errString = "禁止访问";
            } else if (httpEx.code() == 404) {
                errString = "资源不存在";
            } else {
                //其他错误
                errString = "未知网络错误";
            }
        } else {
            errString = throwable.getMessage();
        }
        onError(errString);
    }
}
