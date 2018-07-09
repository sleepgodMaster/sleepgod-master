package com.cool.butterknife.compiler.bean;


import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by cool on 2018/7/5.
 */
public class ElementBean {
    public List<BindViewBean> mBindViewList;
    public List<OnClickBean> mOnClickList;
    public TypeMirror superClassTypeMirror;//父类节点

    public boolean hasSuperClass(){
        return superClassTypeMirror != null;
    }
}