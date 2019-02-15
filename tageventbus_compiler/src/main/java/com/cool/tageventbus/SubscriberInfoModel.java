package com.cool.tageventbus;

import java.util.List;

import javax.lang.model.element.VariableElement;

public class SubscriberInfoModel {
    private String methodName;
    private Subscribe subscribe;
    private List<? extends VariableElement> parameters;

    public SubscriberInfoModel(String methodName, Subscribe subscribe, List<? extends VariableElement> parameters) {
        this.methodName = methodName;
        this.subscribe = subscribe;
        this.parameters = parameters;
    }

    public String getMethodName(){
        return methodName;
    }

    public String getTag(){
        return subscribe.tag();
    }

    public ThreadMode getThreadMode(){
        return subscribe.threadMode();
    }

    public List<? extends VariableElement> getParameters(){
        return parameters;
    }
}
