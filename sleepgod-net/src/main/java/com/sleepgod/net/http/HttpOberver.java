package com.sleepgod.net.http;

import com.sleepgod.net.callback.Callback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
/**
 * Created by cool on 2018/6/20.
 */
public class HttpOberver implements Observer<ResponseBody> {

    private Callback callback;

    public HttpOberver(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (callback != null) {
            callback.onSubscribe(d);
        }
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        if (callback != null)
            callback.onNext(responseBody);
    }

    @Override
    public void onError(Throwable e) {
        if (callback != null)
            callback.onError(e);
    }

    @Override
    public void onComplete() {
        if(callback != null){
            callback.onComplete();
        }
    }
}
