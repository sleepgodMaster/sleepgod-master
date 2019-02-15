package com.cool.tageventbus.meta;

import com.cool.tageventbus.SubscriberMethod;

import java.util.List;

public interface SubscriberInfo {
    Class<?> getSubscriberClass();

    List<SubscriberMethod> getSubscriberMethods();
}
