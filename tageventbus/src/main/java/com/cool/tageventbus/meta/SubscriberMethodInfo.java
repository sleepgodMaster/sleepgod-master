package com.cool.tageventbus.meta;

import com.cool.tageventbus.ThreadMode;

public class SubscriberMethodInfo {
    final String methodName;
    final String tag;
    final Class<?>[] eventTypes;
    final ThreadMode threadMode;


    public SubscriberMethodInfo(String methodName, String tag, Class<?>[] eventTypes, ThreadMode threadMode) {
        this.methodName = methodName;
        this.tag = tag;
        this.eventTypes = eventTypes;
        this.threadMode = threadMode;
    }

    public SubscriberMethodInfo(String methodName, String tag, Class<?>[] eventTypes){
        this(methodName,tag,eventTypes,ThreadMode.POSTING);
    }

    public SubscriberMethodInfo(String methodName, String tag){
        this(methodName,tag,null,ThreadMode.POSTING);
    }
}
