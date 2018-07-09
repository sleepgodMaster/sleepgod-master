package com.cool.butterknife.compiler.bean;

import com.cool.butterknife.annoation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

/**
 * Created by cool on 2018/7/5.
 */
public class BindViewBean {
    private Element element;
    private String simpleClassName;

    public BindViewBean(Element element, String simpleClassName) {
        this.element = element;
        this.simpleClassName = simpleClassName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public Element getElement() {
        return element;
    }

    public int getId() {
        BindView bindView = element.getAnnotation(BindView.class);
        int value = bindView.value();
        return value;
    }

    public String getFiledName() {
        Name filedName = element.getSimpleName();
        return filedName.toString();
    }

}
