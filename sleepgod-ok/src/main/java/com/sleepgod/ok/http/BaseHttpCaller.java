package com.sleepgod.ok.http;

import java.util.Map;

/**
 * 网络调用的基类，H和P是根据使用的网络框架设置的泛型参数，
 * 如果app更换了网络模块，可以像OkHttpCaller那样扩展此抽象类，
 * 根据具体网络模块的api传入泛型参数，使模块可拆装，而不影响调用者。
 * @param <H> Header
 * @param <P> 请求参数
 */
public abstract class BaseHttpCaller<H, P> {

    public abstract H buildHeaders(Map<String, String> headers);

    public abstract P buildPostParams(Map<String, Object> params);



    public String buildUrlParams(Map<String, String> params) {
        StringBuilder paramsBuilder = new StringBuilder("");
        if (params != null) {
            paramsBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            return paramsBuilder.deleteCharAt(paramsBuilder.length() - 1).toString();
        }
        return paramsBuilder.toString();
    }



}
