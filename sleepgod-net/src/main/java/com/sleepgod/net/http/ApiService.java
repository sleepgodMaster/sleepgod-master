package com.sleepgod.net.http;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by cool on 2018/6/20.
 * retrofit的所有功能
 */

public interface ApiService {
    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> params,@HeaderMap HashMap<String,String> hashMap);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> params,@HeaderMap HashMap<String,String> hashMap);

    @FormUrlEncoded
    @PUT
    Observable<ResponseBody> put(@Url String url, @FieldMap Map<String, Object> params,@HeaderMap HashMap<String,String> hashMap);

    @DELETE
    Observable<ResponseBody> delete(@Url String url, @QueryMap Map<String, Object> params,@HeaderMap HashMap<String,String> hashMap);

    //下载是直接到内存,所以需要 @Streaming
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params,@HeaderMap HashMap<String,String> hashMap);

    //上传
    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @Part MultipartBody.Part file,@HeaderMap HashMap<String,String> hashMap);

    //原始数据
    @POST
    Observable<ResponseBody> postJson(@Url String url, @Body RequestBody body,@HeaderMap HashMap<String,String> hashMap);

    @PUT
    Observable<ResponseBody> putJson(@Url String url, @Body RequestBody body,@HeaderMap HashMap<String,String> hashMap);

}










