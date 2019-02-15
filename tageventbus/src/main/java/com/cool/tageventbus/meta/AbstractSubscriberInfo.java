package com.cool.tageventbus.meta;

import com.cool.tageventbus.EventBusException;
import com.cool.tageventbus.SubscriberMethod;
import com.cool.tageventbus.ThreadMode;

import java.lang.reflect.Method;

public abstract class AbstractSubscriberInfo implements SubscriberInfo {
    private final Class subscriberClass;

    protected AbstractSubscriberInfo(Class subscriberClass) {
        this.subscriberClass = subscriberClass;
    }

    @Override
    public Class getSubscriberClass() {
        return subscriberClass;
    }

    protected SubscriberMethod createSubscriberMethod(String tag, String methodName, Class<?>[] eventTypes, ThreadMode threadMode){
        if(subscriberClass == null){
            return null;
        }

        try {
            Method method = subscriberClass.getDeclaredMethod(methodName, eventTypes);
            return new SubscriberMethod(method,tag,eventTypes,threadMode);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new EventBusException("Could not find subscriber method in " + subscriberClass +
                    ". Maybe a missing ProGuard rule?");
        }
    }
}
