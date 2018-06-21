package com.sleepgod.net.callback;

import com.google.gson.Gson;
import com.sleepgod.net.base.presenter.BasePresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by cool on 2018/6/20.
 */
public abstract class HttpCallback<ResultType> extends ErrorHandler implements Callback<ResultType> {
    private boolean mShowLodding;
    private String mTitle;
    private WeakReference<BasePresenter> mPresenterWReference;

    public void init(WeakReference<BasePresenter> presenterWReference, boolean showLodding, String title) {
        this.mPresenterWReference = presenterWReference;
        this.mShowLodding = showLodding;
        this.mTitle = title;
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        if(mPresenterWReference != null && mPresenterWReference.get() != null){
            mPresenterWReference.get().addDisposable(disposable);
        }
        onStart();
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        parseResponse(responseBody);
    }

    @Override
    public void onError(Throwable throwable) {
        handleError(throwable);
        onComplete();
    }

    @Override
    public void onComplete() {
        if (mShowLodding && mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().mView.hideLodingDialog();
        }
    }

    private void onStart() {
        if (mShowLodding && mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().mView.showLodingDialog(mTitle);
        }
    }

    private void parseResponse(ResponseBody responseBody) {
        if (responseBody == null) {
            onError("客户端异常");
            return;
        }

        Type genType = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        try {
            if (type == ResponseBody.class) {
                onSuccess((ResultType) responseBody);
            } else if (type == String.class) {
                onSuccess((ResultType) responseBody.string());
            } else {
                ResultType resultType = new Gson().fromJson(responseBody.string(), type);
                onSuccess(resultType);
            }
        } catch (IOException e) {
            handleError(e);
        }finally {
            responseBody.close();
        }
    }

    public abstract void onSuccess(ResultType resultType);

    public abstract void onError(String msg);
}
