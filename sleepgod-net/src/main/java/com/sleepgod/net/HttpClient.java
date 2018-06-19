package com.sleepgod.net;

import com.sleepgod.net.base.presenter.BasePresenter;
import com.sleepgod.net.http.Callback;
import com.sleepgod.net.http.HttpMethod;
import com.sleepgod.net.http.Request;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class HttpClient {
    private WeakReference<BasePresenter> presenterWReference;
    private boolean showLoddingDialog;
    private String title;
    private HttpMethod httpMethod;
    private HashMap<String, Object> params;
    private String url;
    private String baseUrl;
    private File uploadFile;

    private HttpClient(Builder builder) {
        this.presenterWReference = builder.presenterWReference;
        this.showLoddingDialog = builder.showLoddingDialog;
        this.title = builder.title;
        this.httpMethod = builder.httpMethod;
        this.params = builder.params;
        this.url = builder.url;
        this.baseUrl = builder.baseUrl;
        this.uploadFile = builder.uploadFile;
    }

    public <T> void callback(Callback<T> callback){
        callback.init(presenterWReference,showLoddingDialog,title);
        Request.newRequest(httpMethod,params,url,baseUrl,uploadFile,callback);
    }


    public static Builder create(BasePresenter basePresenter) {
        return new Builder(basePresenter);
    }

    public static class Builder {
        private WeakReference<BasePresenter> presenterWReference;
        private boolean showLoddingDialog;
        private String title;
        private HttpMethod httpMethod = HttpMethod.GET;
        private HashMap<String, Object> params;
        private String url;
        private String baseUrl;
        private File uploadFile;

        public Builder(BasePresenter basePresenter) {
            presenterWReference = new WeakReference<>(basePresenter);
        }

        public Builder showLoddingDialog(boolean showLoddingDialog) {
            this.showLoddingDialog = showLoddingDialog;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder get() {
            httpMethod = HttpMethod.GET;
            return this;
        }

        public Builder post() {
            httpMethod = HttpMethod.POST;
            return this;
        }

        public Builder upload(File uploadFile){
            this.uploadFile = uploadFile;
            httpMethod = HttpMethod.UPLOAD;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = url;
            return this;
        }

        public Builder params(HashMap<String, Object> params) {
            this.params = params;
            return this;
        }

        public HttpClient builder() {
            checkup();
            return new HttpClient(this);
        }

        private void checkup() {
            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url cannot be null");
            }
        }
    }
}
