package com.cool.butterknife.core;

import android.support.annotation.UiThread;

/**
 * Created by cool on 2018/7/3.
 */
public interface Unbinder {
    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override public void unbind() { }
    };
}
