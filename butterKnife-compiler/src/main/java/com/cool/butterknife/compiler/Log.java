package com.cool.butterknife.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by cool on 2018/7/5.
 */
public class Log {
    private Messager messager;

    public Log(Messager messager) {
        this.messager = messager;
    }

    public void e(String msg){
        messager.printMessage(Diagnostic.Kind.NOTE,msg);
    }
}
