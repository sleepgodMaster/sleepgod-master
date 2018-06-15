package com.sleepgod.ok.parse;


import com.sleepgod.ok.exception.TokenExpiredException;

import org.json.JSONException;

/**
 * 数据解析接口
 * @param <B> 解析出的实体类
 */
public interface IParser<B>  {


    B parse(String response) throws JSONException, TokenExpiredException;
}
