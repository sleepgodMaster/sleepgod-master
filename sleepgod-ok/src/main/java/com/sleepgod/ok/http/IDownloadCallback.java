package com.sleepgod.ok.http;

/**
 * 下载文件回调接口
 */
public interface IDownloadCallback {

    void onStart();

    void onProgress(long contentLength, int readLength, byte[] bytes);

    void onFinish();

    void onError(Exception e);

}
