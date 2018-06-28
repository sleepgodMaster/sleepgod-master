package com.sleepgod.net.http;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cool on 2018/6/20.
 */

public class RetrofitCreator {
    private static volatile RetrofitCreator instance;
    private Map<String, Retrofit> mRetrofitCache = new HashMap<>();
    HttpConfig.Builder configBuilder;

    private RetrofitCreator() {
    }

    public static RetrofitCreator getInstance() {
        if (instance == null) {
            synchronized (RetrofitCreator.class) {
                if (instance == null) {
                    instance = new RetrofitCreator();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        if(TextUtils.isEmpty(configBuilder.BASE_URL)){
            throw new IllegalArgumentException("please config baseUrl");
        }
        Retrofit retrofit = retrofit(configBuilder.BASE_URL);
        return retrofit;
    }

    public Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = retrofit(baseUrl);
        return retrofit;
    }

    private Retrofit retrofit(String baseUrl) {
        Retrofit retrofit = mRetrofitCache.get(baseUrl);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getOkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            mRetrofitCache.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(configBuilder.readTimeout, configBuilder.readTimeUnit)
                .writeTimeout(configBuilder.writeTimeout, configBuilder.writeTimeUnit)
                .connectTimeout(configBuilder.connTimeout, configBuilder.connTimeUnit);
        for (Interceptor interceptor : configBuilder.INTERCEPTORS) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    public ApiService createService() {
        return getRetrofit().create(ApiService.class);
    }

    public ApiService createService(String baseUrl) {
        return getRetrofit(baseUrl).create(ApiService.class);
    }

    public void initBuilder(HttpConfig.Builder builder) {
        this.configBuilder = builder;
    }
}
