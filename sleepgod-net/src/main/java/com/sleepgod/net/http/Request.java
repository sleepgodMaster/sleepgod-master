package com.sleepgod.net.http;

import android.text.TextUtils;

import com.sleepgod.net.ApiService;
import com.sleepgod.net.RetrofitCreator;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class Request {
    private HttpMethod httpMethod;
    private HashMap<String, Object> params;
    private String url;
    private String baseUrl;
    private File uploadFile;

    private  <T> Request(HttpMethod httpMethod, HashMap<String, Object> params, String url, String baseUrl, File uploadFile, Callback<T> callback) {
        this.httpMethod = httpMethod;
        this.params = params;
        this.url = url;
        this.baseUrl = baseUrl;
        this.uploadFile = uploadFile;
        request(httpMethod,callback);
    }

    public static <T> Request newRequest(HttpMethod httpMethod, HashMap<String, Object> params, String url, String baseUrl, File uploadFile, Callback<T> callback){
        return new Request(httpMethod,params,url,baseUrl,uploadFile,callback);
    }

    private ObservableTransformer getComposer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private <T> void request(HttpMethod httpMethod, Callback<T> callback) {
        RetrofitCreator retrofitCreator = RetrofitCreator.getInstance();
        ApiService service;
        if (!TextUtils.isEmpty(baseUrl)) {
            service = retrofitCreator.createService(baseUrl);
        } else {
            service = retrofitCreator.createService();
        }

        Observable<ResponseBody> observable = null;

        switch (httpMethod) {
            case GET:
                observable = service.get(url,params);
                break;
            case POST:
                observable = service.post(url,params);
                break;
            case PUT:
                observable = service.put(url,params);
                break;
            case DELETE:
                observable = service.delete(url,params);
                break;
            case UPLOAD:
                if(uploadFile == null){
                    throw new IllegalArgumentException("file cannot be null");
                }
                final MultipartBody.Part body=MultipartBody.Part.createFormData(
                        "file",uploadFile.getName());
                observable = service.upload(url,body);
                break;
            case DOWNLOAD:
                downLoad();
                return;
        }
        if(observable != null){
            toSubscribe(observable,callback);
        }

    }

    private <T> void toSubscribe(Observable<ResponseBody> observable, Callback<T> callback) {
        observable.compose(getComposer())
                .subscribe(new HttpOberver(callback));
    }

    private void downLoad() {

    }
}
