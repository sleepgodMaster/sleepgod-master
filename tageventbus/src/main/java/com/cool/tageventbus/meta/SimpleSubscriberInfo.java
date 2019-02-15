package com.cool.tageventbus.meta;


import com.cool.tageventbus.SubscriberMethod;

import java.util.ArrayList;
import java.util.List;

public class SimpleSubscriberInfo extends AbstractSubscriberInfo {

    private final List<SubscriberMethodInfo> methodInfos;

    public SimpleSubscriberInfo(Class subscriberClass, List<SubscriberMethodInfo> methodInfos) {
        super(subscriberClass);
        this.methodInfos = methodInfos;
    }

    @Override
    public synchronized List<SubscriberMethod> getSubscriberMethods() {
        if(methodInfos == null || methodInfos.size() <=0){
            return null;
        }

        List<SubscriberMethod> subscriberMethodList = new ArrayList<>();

        for (SubscriberMethodInfo methodInfo : methodInfos) {
            SubscriberMethod subscriberMethod = createSubscriberMethod(methodInfo.tag, methodInfo.methodName, methodInfo.eventTypes, methodInfo.threadMode);
            subscriberMethodList.add(subscriberMethod);
        }
        return subscriberMethodList;
    }
}
