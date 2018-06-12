package com.sleepgod.sleepgod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitManager {

    private static volatile RetrofitManager instance;

    private List<String> mUrlCaches = new ArrayList<>();
    private Map<String, Retrofit> mRetrofitCache = new HashMap<>();
    private final static String BASE_URL = "http://199.10.200.152:8099/Test/";

    private RetrofitManager() {

    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        Retrofit retrofit = mRetrofitCache.get(BASE_URL);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient())
                    .build();
            mRetrofitCache.put(BASE_URL, retrofit);
        }
        return retrofit;
    }

    public Retrofit getRetrofit(String baseUrl) {

        return null;
    }

}
