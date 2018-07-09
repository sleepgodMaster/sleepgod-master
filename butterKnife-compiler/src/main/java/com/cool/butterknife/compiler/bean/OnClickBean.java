package com.cool.butterknife.compiler.bean;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * Created by cool on 2018/7/5.
 */
public class OnClickBean {
    private Element element;
    private String simpleClassName;
    private int id;
    private List<? extends VariableElement> parameters;//参数类型

    public OnClickBean(Element element,String simpleClassName,int id,List<? extends VariableElement> parameters) {
        this.element = element;
        this.simpleClassName = simpleClassName;
        this.id = id;
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public Element getElement() {
        return element;
    }


    public String getMethodName(){
        return element.getSimpleName().toString();
    }

    public List<? extends VariableElement> getParameters() {
        return parameters;
    }
}
