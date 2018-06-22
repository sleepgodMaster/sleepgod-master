package com.sleepgod.net.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sleepgod.net.callback.Callback;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by cool on 2018/6/20.
 */
public class Request {
    private HashMap<String, Object> params;
    private HashMap<String, String> heads;
    private Object requestBean;
    private String url;
    private String baseUrl;
    private File uploadFile;

    private <T> Request(HttpMethod httpMethod, HashMap<String, Object> params,
                        HashMap<String, String> heads, Object requestBean, String url,
                        String baseUrl, File uploadFile, Callback<T> callback) {
        this.params = params;
        this.heads = heads;
        this.requestBean = requestBean;
        this.url = url;
        this.baseUrl = baseUrl;
        this.uploadFile = uploadFile;
        request(httpMethod, callback);
    }

    public static <T> Request newRequest(HttpMethod httpMethod, HashMap<String, Object> params,
                                         HashMap<String, String> heads, Object requestBean, String url, String baseUrl,
                                         File uploadFile, Callback<T> callback) {
        return new Request(httpMethod, params, heads, requestBean, url, baseUrl, uploadFile, callback);
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

        if (params == null) {
            params = new HashMap<>();
        }

        if (heads == null) {
            heads = new HashMap<>();
        }

        Observable<ResponseBody> observable = null;
        getRequestParams();

        switch (httpMethod) {
            case GET:
                observable = service.get(url, params, heads);
                break;
            case POST:
                observable = service.post(url, params, heads);
                break;
            case PUT:
                observable = service.put(url, params, heads);
                break;
            case DELETE:
                observable = service.delete(url, params, heads);
                break;
            case UPLOAD:
                MultipartBody.Part body = getFilePart();
                observable = service.upload(url, body, heads);
                break;
            case DOWNLOAD:
                observable = service.download(url, params, heads);
                break;
            case POST_JSON: {
                RequestBody requestBody = getJsonRequestBody();
                observable = service.postJson(url, requestBody, heads);
            }
            break;
            case PUT_JSON: {
                RequestBody requestBody = getJsonRequestBody();
                observable = service.putJson(url, requestBody, heads);
            }
            break;
        }
        if (observable != null) {
            toSubscribe(observable, callback);
        }

    }

    private void getRequestParams() {
        if (requestBean != null) {
            try {
                Class<?> requestBeanClass = requestBean.getClass();
                Field[] declaredFields = requestBeanClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(requestBean);
                    params.put(name, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private MultipartBody.Part getFilePart() {
        if (uploadFile == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);
        return MultipartBody.Part.createFormData(
                "file", uploadFile.getName(), requestFile);
    }

    @NonNull
    private RequestBody getJsonRequestBody() {
        MediaType textType = MediaType.parse("application/json");
        return RequestBody.create(textType, new Gson().toJson(params));
    }

    private <T> void toSubscribe(Observable<ResponseBody> observable, Callback<T> callback) {
        observable.compose(getComposer())
                .subscribe(new HttpOberver(callback));
    }
}
