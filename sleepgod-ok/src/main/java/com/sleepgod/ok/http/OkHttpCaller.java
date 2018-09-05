package com.sleepgod.ok.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sleepgod.ok.exception.NetUnavailableException;
import com.sleepgod.ok.util.AppLog;
import com.sleepgod.ok.util.NetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp网络框架的封装层
 *
 * @param <B> 解析网络数据后构造的实体类
 */
public class OkHttpCaller<B> extends BaseHttpCaller<Headers, RequestBody> {

  private static OkHttpCaller instance = null;

  private OkHttpClient mOkHttpClient = null;

  private Handler mHandler = null;

  private static Context context = null;

  //private final int TIMEOUT = 5_000;

  private OkHttpCaller() {
    mOkHttpClient = new OkHttpClient();
    mHandler = new Handler(Looper.getMainLooper());
  }

  //    public void setTimeOut(int timeOut) {
  //        mOkHttpClient.setConnectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
  //        mOkHttpClient.setReadTimeout(timeOut, TimeUnit.MILLISECONDS);
  //        mOkHttpClient.setWriteTimeout(timeOut, TimeUnit.MILLISECONDS);
  //    }

  public synchronized static OkHttpCaller getInstance(Context cxt) {
    if (instance == null) {
      instance = new OkHttpCaller();
      context = cxt;
    }
    return instance;
  }

  public void get(String url, Map<String, String> headers, Map<String, String> params,Class<?> clazz,
                  IResponseCallback callback) {
    AppLog.e("get params = " + params.toString());
    Request request = new Request.Builder().headers(buildHeaders(headers))
            .url(url + buildUrlParams(params))
            .get()
            .build();

    execute(request,clazz,callback);
  }



  public void post(String url, Map<String, String> headers, String json,Class<?> clazz,
                   IResponseCallback callback) {
    AppLog.e("json params = " + json);
    MediaType type = MediaType.parse("application/json; charset=utf-8");
    Request request = new Request.Builder().url(url)
            .headers(buildHeaders(headers))
            .post(RequestBody.create(type, json))
            .build();

    execute(request,clazz,callback);
  }


  public void uploadFile(String url, List<File> flies, HashMap<String, Object> params,Class<?> clazz,
                         IResponseCallback callback) {
    MultipartBody.Builder builder = new MultipartBody.Builder();
    builder.setType(MultipartBody.FORM);
    if (flies != null) {
      for (int i = 0; i < flies.size(); i++) {
        File file = flies.get(i);
        String fileName = file.getName();
        RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
        //TODO 根据文件名设置contentType
        builder.addFormDataPart(fileName, fileName, fileBody);//对应spring框架
      }
    }
    //添加参数
    for (String key : params.keySet()) {
      Object object = params.get(key);
      if (!(object instanceof File)) {
        if (object == null) {
          builder.addFormDataPart(key, "");
        } else {
          builder.addFormDataPart(key, object.toString());
        }
      } else {
        File file = (File) object;
        builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
      }
    }

    RequestBody requestBody = builder.build();
    Request request = new Request.Builder().url(url).post(requestBody).build();
    execute(request,clazz,callback);
  }


  private String guessMimeType(String path) {
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    String contentTypeFor = null;
    try {
      contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (contentTypeFor == null) {
      contentTypeFor = "application/octet-stream";
    }
    return contentTypeFor;
  }


  public void download(String url, String filePath, Map<String, String> headers,
                       Map<String, String> params, IDownloadCallback callback) {
    Request request = new Request.Builder().headers(buildHeaders(headers))
            .url(url + buildUrlParams(params))
            .build();

    execute(request, filePath, callback);
  }




  /**
   * 执行普通网络请求
   */
  private void execute(Request request, final Class<?> clazz, final IResponseCallback callback) {
    AppLog.e("request = " + request.toString());

    if (!NetUtil.isNetAvailable(context)) {
      postFailure(callback, new NetUnavailableException("Unavailable network"));
      return;
    }

    callback.onStart();
    mOkHttpClient.newCall(request).enqueue(new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {
        postFailure(callback, e);
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {

        String responseStr = response.body().string();
        AppLog.e("responseStr = " + responseStr);
        if (TextUtils.isEmpty(responseStr)) {
          postFailure(callback, new NullPointerException());
        } else {
          if (clazz == null) {
            postResponse(callback,responseStr);
          }else{
            Gson mGson = new Gson();
            Object obj = mGson.fromJson(responseStr, clazz);
            postResponse(callback,obj);
          }

          response.body().close();
        }
      }

    });
  }


  /**
   * 执行下载任务
   */
  private void execute(final Request request, String filePath, final IDownloadCallback callback) {
    AppLog.e("request = " + request.toString());

    if (!NetUtil.isNetAvailable(context)) {
      callback.onError(new IOException("No content to be downloaded"));
      return;
    }
    if (TextUtils.isEmpty(filePath)) {
      callback.onError(new IOException("No content to be downloaded"));
      return;
    }
    final File file = new File(filePath);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    AppLog.e("Download started.");
    callback.onStart();

    mOkHttpClient.newCall(request).enqueue(new Callback() {

      @Override public void onFailure(Call call, IOException e){
        callback.onError(e);//子线程中回调错误处理的方法，调用者需回到主线程中处理UI逻辑
      }

      @Override public void onResponse(Call call, Response response) throws IOException {//non-main thread
        FileOutputStream output = null;
        InputStream inputStream = null;
        int readSize = 0;
        try {
          output = new FileOutputStream(file);
          inputStream = response.body().byteStream();
          long total = response.body().contentLength();
          if (total < 0) {
            /** 子线程中回调错误处理的方法，调用者需回到主线程中处理UI逻辑 */
            callback.onError(new IOException("No content to be downloaded"));
          } else {
            //                        byte[] buffer = new byte[(int)Math.min(total / 100, Integer.MAX_VALUE)];
            byte[] buffer = new byte[1024];
            int l = -1;
            while ((l = inputStream.read(buffer)) != -1) {
              /** 子线程中回调下载进度的方法，调用者需回到主线程中处理UI逻辑 */
              readSize += l;
              callback.onProgress(total, readSize, buffer);
              output.write(buffer, 0, l);
              //                            AppLog.e("content length = " + total + ", read length = " + readSize);
            }
            callback.onFinish();//子线程中回调下载结束的方法，调用者需回到主线程中处理UI逻辑
            //                        AppLog.e("Download finished.");
            output.flush();
            response.body().close();
          }
        } finally {
          if (output != null) {
            output.close();
            output = null;
          }
          if (inputStream != null) {
            inputStream.close();
            inputStream = null;
          }
        }
      }
    });
  }


  /**
   * 执行图片下载任务
   */
  private void execute(final Request request, final IDownloadImgCallback callback) {
    AppLog.e("request = " + request.toString());

    if (!NetUtil.isNetAvailable(context)) {
      callback.onError(new IOException("No content to be downloaded"));
      return;
    }

    AppLog.e("Download started.");
    callback.onStart();

    mOkHttpClient.newCall(request).enqueue(new Callback() {

      @Override public void onFailure(Call call, IOException e) {
        AppLog.e("total===" + e.getMessage());
        callback.onError(e);//子线程中回调错误处理的方法，调用者需回到主线程中处理UI逻辑
      }

      @Override public void onResponse(Call call, Response response) throws IOException {//non-main thread
        AppLog.e("response===!!!!" + response);
        InputStream inputStream = response.body().byteStream();
        long total = response.body().contentLength();
        postImgResponse(callback, BitmapFactory.decodeStream(inputStream));
        AppLog.e("Download finished.");
        response.body().close();
        AppLog.e("total===" + total);
      }
    });
  }

  /**
   * 到主线程中回调错误处理的方法
   */
  public void postFailure(final IResponseCallback callback, final Exception e) {
    if (callback != null) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          callback.onError(e);
        }
      });
    }
  }



  /**
   * 到主线程中回调数据返回的方法
   */
  public void postResponse(final IResponseCallback callback, final Object response) {
    if (callback != null) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          callback.onFinish(response);
        }
      });
    }
  }


  /**
   * 到主线程中回调数据返回的方法
   */
  public void postImgResponse(final IDownloadImgCallback callback, final Bitmap b) {
    if (callback != null) {
      mHandler.post(new Runnable() {
        @Override public void run() {
          callback.onFinsh(b);
        }
      });
    }
  }


  @Override
  public Headers buildHeaders(Map<String, String> headers) {
    return Headers.of(headers);
  }

  @Override
  public RequestBody buildPostParams(Map<String, Object> params) {
    FormBody.Builder builder =  new FormBody.Builder();
    if (params != null) {
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        if (entry.getValue() != null) builder.add(entry.getKey(), entry.getValue() + "");
      }
    }
    return builder.build();
  }
}
