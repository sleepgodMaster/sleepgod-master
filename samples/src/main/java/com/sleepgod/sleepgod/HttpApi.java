package com.sleepgod.sleepgod;

import android.content.Context;
import android.text.TextUtils;

import com.sleepgod.ok.http.IResponseCallback;
import com.sleepgod.ok.http.OkHttpCaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * okhttp 的总控制
 * Created by cuiweicai on 2018/6/14.
 */

public class HttpApi {

    private static OkHttpCaller okHttpCaller;

    private static HeaderBuilder headerBuilder = null;

    static {
        okHttpCaller = OkHttpCaller.getInstance(MyApplication.getApplication());
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
        Context context = MyApplication.getApplication();
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
    public static JSONObject buildRequestParams() {
        JSONObject params = new JSONObject();
        try {
          //  if (SessionManager.getInstance().isLogin()) {
                params.put("token", "");
                params.put("userId", "");
          //  }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static void post(String url, JSONObject jsonObject, IResponseCallback callback) {
        okHttpCaller.post(url, getHeaderBuilder().build(), jsonObject.toString(), callback);
    }

    public static void get(String url, Map<String, Object> params, IResponseCallback callback) {
        okHttpCaller.get(url, getHeaderBuilder().build(), params,callback);
    }



}
