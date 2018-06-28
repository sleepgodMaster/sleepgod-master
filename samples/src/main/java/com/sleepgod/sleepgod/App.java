package com.sleepgod.sleepgod;

import android.app.Application;

import com.sleepgod.net.http.HttpConfig;
import com.sleepgod.ok.util.AppLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cuiweicai on 2018/6/14.
 */

public class App extends Application {
    public static App mApp;

    public static App getApplication() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        AppLog.setDEBUG(true);
        initHttpClient();
    }

    private void initHttpClient() {
        HttpConfig.create()
                .baseUrl("http://api.shujuzhihui.cn/")
                .connTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .builder();
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            AppLog.e(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            AppLog.e(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }
}
