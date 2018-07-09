package com.cool.butterknife.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cool on 2018/7/3.
 */
public class ButterKnife {

    static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap<>();

    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Activity target) {
        View sourceView = target.getWindow().getDecorView();
        return createBinding(target, sourceView);
    }

    public static Unbinder bind(@NonNull Fragment target,View sourceView){
        return createBinding(target,sourceView);
    }

    public static Unbinder bind(@NonNull android.app.Fragment target, View sourceView){
        return createBinding(target,sourceView);
    }

    private static Unbinder createBinding(Object target, View sourceView) {
        Class<?> targetClass = target.getClass();
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);
        if (constructor == null) {
            return Unbinder.EMPTY;
        }
        try {
            return constructor.newInstance(target, sourceView);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> targetClass) {
        Constructor<? extends Unbinder> constructor = BINDINGS.get(targetClass);
        if (constructor != null) {
            return constructor;
        }

        String targetClassName = targetClass.getName();
        try {
            Class<?> viewBindingClass = Class.forName(targetClassName + "_ViewBinding");
            constructor = (Constructor<? extends Unbinder>) viewBindingClass.getConstructor(targetClass, View.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BINDINGS.put(targetClass, constructor);

        return constructor;
    }
}
