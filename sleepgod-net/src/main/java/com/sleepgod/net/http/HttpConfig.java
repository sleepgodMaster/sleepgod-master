package com.sleepgod.net.http;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * Created by cool on 2018/6/28.
 */
public class HttpConfig {

    private HttpConfig(Builder builder) {
        RetrofitCreator.getInstance().initBuilder(builder);
    }

    public static Builder create(){
        return new Builder();
    }

    public static class Builder{
        public static final List<Interceptor> INTERCEPTORS = new ArrayList<>();
        public String BASE_URL;
        public long readTimeout = 60;//读取超时时间
        public TimeUnit readTimeUnit = TimeUnit.SECONDS;
        public long writeTimeout = 60;//写入超时时间
        public TimeUnit writeTimeUnit = TimeUnit.SECONDS;
        public long connTimeout = 60;//连接超时时间
        public TimeUnit connTimeUnit = TimeUnit.SECONDS;

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl cannot be null");
            }
            BASE_URL = baseUrl;
            return this;
        }

        public Builder readTimeout(long readTimeout, TimeUnit timeUnit) {
            this.readTimeout = readTimeout;
            this.readTimeUnit = timeUnit;
            return this;
        }

        public Builder writeTimeout(long writeTimeout, TimeUnit timeUnit) {
            this.writeTimeout = writeTimeout;
            this.writeTimeUnit = timeUnit;
            return this;
        }

        public Builder connTimeout(long connTimeout, TimeUnit timeUnit) {
            this.connTimeout = connTimeout;
            this.connTimeUnit = timeUnit;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            INTERCEPTORS.add(interceptor);
            return this;
        }

        public void builder(){
            new HttpConfig(this);
        }
    }
}
