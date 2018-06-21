package com.sleepgod.net.callback;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by cool on 2018/6/20.
 */

public interface Callback<T> {
    void onSubscribe(Disposable disposable);
    void onNext(ResponseBody responseBody);
    void onError(Throwable e);
    void onComplete();
}
