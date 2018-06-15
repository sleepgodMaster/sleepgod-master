package com.sleepgod.ok.http;

import android.graphics.Bitmap;

/**
 * 2016/5/13
 */
public interface IDownloadImgCallback {
    void onStart();

    void onFinsh(Bitmap b);

    void onError(Exception e);
}
