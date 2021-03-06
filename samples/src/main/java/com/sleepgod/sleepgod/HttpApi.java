package com.sleepgod.sleepgod;

import android.content.Context;

import com.google.gson.Gson;
import com.sleepgod.ok.http.IDownloadCallback;
import com.sleepgod.ok.http.IResponseCallback;
import com.sleepgod.ok.http.OkHttpCaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * okhttp 的总控制
 * Created by cuiweicai on 2018/6/14.
 */

public class HttpApi {

    private static OkHttpCaller okHttpCaller;

    private static HeaderBuilder headerBuilder = null;

    static {
        okHttpCaller = OkHttpCaller.getInstance(App.getApplication());
    }

    private static class HeaderBuilder {

        private Map<String, String> mHeaders = null;

        public HeaderBuilder() {
            this.mHeaders = new HashMap<>();
        }

        public HeaderBuilder put(String key, String Value) {
            mHeaders.put(key, Value);
            return this;
        }

        public Map<String, String> build() {
            return mHeaders;
        }
    }

    /**
     * 请求头
     */
    public static HeaderBuilder getHeaderBuilder() {
        Context context = App.getApplication();
        headerBuilder = new HeaderBuilder()
                .put("content-type", "application/json")
                .put("timestamp", String.valueOf(System.currentTimeMillis()))
                .put("device", "Android");


        return headerBuilder;
    }

    /**
     * 公共参数
     * @return
     */
    public static Map buildRequestParams() {
        Map<String, Object> params = new HashMap<>();

//                params.put("token", "");

        return params;
    }



    public static void post(String url, Map<String, Object> params,Class<?> clazz, IResponseCallback callback) {
        String jsonObject = new Gson().toJson(params);
        okHttpCaller.post(url, getHeaderBuilder().build(), jsonObject,clazz, callback);
    }

    public static void get(String url, Map<String, Object> params,Class<?> clazz,  IResponseCallback callback) {
        okHttpCaller.get(url, getHeaderBuilder().build(), params,clazz,callback);
    }

    public static void uploadFile(String url, List<File> files, HashMap<String, Object> params,Class<?> clazz,  IResponseCallback callback) {
        okHttpCaller.uploadFile(url,files , params,clazz,callback);
    }

    public static void download(String url, String filePath, HashMap<String, Object> params, IDownloadCallback callback) {
        okHttpCaller.download(url,filePath , getHeaderBuilder().build(),params,callback);
    }
}
