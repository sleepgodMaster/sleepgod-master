package com.sleepgod.net.callback;

import com.google.gson.JsonParseException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

/**
 * Created by cool on 2018/6/21.
 */
public abstract class ErrorHandler {
    /**
     * 处理错误信息
     * @param throwable
     */
    public void handleError(Throwable throwable) {
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
            } else if(httpEx.code() == 401){
                errString = "未授权";
            }else if (httpEx.code() == 403) {
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

    protected abstract void onError(String errString);
}
