package com.sleepgod.net;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitCreator {
    private static volatile RetrofitCreator instance;
    private Map<String, Retrofit> mRetrofitCache = new HashMap<>();
    private final static String BASE_URL = "http://gank.io/";

    private RetrofitCreator(){

    }

    public static RetrofitCreator getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new RetrofitCreator();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        Retrofit retrofit = retrofit(BASE_URL);
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
                    .client(new OkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            mRetrofitCache.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    public ApiService createService(){
        return getRetrofit().create(ApiService.class);
    }

    public ApiService createService(String baseUrl){
        return getRetrofit(baseUrl).create(ApiService.class);
    }

}
