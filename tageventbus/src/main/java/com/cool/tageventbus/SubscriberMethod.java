package com.cool.tageventbus;

import java.lang.reflect.Method;

public class SubscriberMethod {
    public Method method;
    public String tag;
    public Class<?>[] eventTypes;
    public ThreadMode threadMode;

    public SubscriberMethod(Method method, String tag, Class<?>[] eventTypes, ThreadMode threadMode) {
        this.method = method;
        this.tag = tag;
        this.eventTypes = eventTypes;
        this.threadMode = threadMode;
    }
}
