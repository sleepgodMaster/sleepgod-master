package com.sleepgod.net.http;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HttpClient {



    public static ObservableTransformer getComposer(){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 订阅
     *
     * @param observable 被监听者，相当于网络访问
     * @param observer 监听者，  相当于回调监听
     */
    private static <T> void toSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.compose(getComposer()).subscribe(observer);//订阅
    }

    /**
     * 发起post请求
     */
    public static void post(Observer observer){

    }

    /**
     * 发起get请求
     */
    public static void get(){

    }
}
