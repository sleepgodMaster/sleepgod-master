package com.sleepgod.permission.aspect;

import android.app.Fragment;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.sleepgod.permission.PermissionUtils;
import com.sleepgod.permission.annotation.APermission;
import com.sleepgod.permission.annotation.APermissionDenied;
import com.sleepgod.permission.annotation.APermissionRationale;
import com.sleepgod.permission.permission.Permission;
import com.sleepgod.permission.permission.PermissionActivity;
import com.sleepgod.permission.permission.PermissionResult;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by cool on 2018/6/25.
 */
@Aspect
public class PermissionAspect implements PermissionActivity.OnPermissionCallback {
    private ProceedingJoinPoint joinPoint;
    private APermission aPermission;
    private Context context;

    @Pointcut("execution(@com.sleepgod.permission.annotation.APermission * *(..))  && @annotation(aPermission)")
    public void methodAnnotationWithAPermission(APermission aPermission) {
    }

    @Around("methodAnnotationWithAPermission(aPermission)")
    public void permission(final ProceedingJoinPoint joinPoint, APermission aPermission) throws Throwable {
        this.joinPoint = joinPoint;
        this.aPermission = aPermission;
        String[] permissions = aPermission.permissions();
        Object target = joinPoint.getTarget();
        if (target instanceof Context) {
            context = ((Context) target).getApplicationContext();
        } else if (target instanceof Fragment) {
            context = ((Fragment) target).getActivity().getApplicationContext();
        } else if (target instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) target).getActivity().getApplicationContext();
        } else {
            context = PermissionUtils.getContext();
        }
        if (context == null) {
            return;
        }

        PermissionResult permissionResult = PermissionUtils.checkPermissions(context, permissions);
        //如果有权限，则直接调用
        if (permissionResult.isHasPermission()) {
            onGranted();
            return;
        }

        PermissionActivity.requestPermission(context, permissions, aPermission.showRationaleDialog(), this);
    }

    @Override
    public void onGranted() {
        try {
            joinPoint.proceed();
            joinPoint = null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onRationale(List<String> roationaleList) {
        Method permissionRationaleMethod = getPermissionRationaleMethod(joinPoint.getTarget());
        invoke(joinPoint.getTarget(), permissionRationaleMethod, roationaleList, aPermission.requestCode());
        joinPoint = null;
    }

    @Override
    public void onDenied(List<String> deniedList) {
        Method permissionDeniedMethod = getPermissionDeniedMethod(joinPoint.getTarget());
        if (permissionDeniedMethod == null && !TextUtils.isEmpty(aPermission.deniedMessage())) {
            Toast.makeText(context, aPermission.deniedMessage(), Toast.LENGTH_SHORT).show();
        } else {
            invoke(joinPoint.getTarget(), permissionDeniedMethod, deniedList, aPermission.requestCode());
        }
        joinPoint = null;
    }

    private void invoke(Object tagert, Method method, List<String> permissions, int requestCode) {
        try {
            if (method != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0) {
                    method.invoke(tagert);
                } else if (parameterTypes.length == 1 && Permission.class.isAssignableFrom(parameterTypes[0])) {
                    Permission permission = new Permission(permissions, requestCode);
                    method.invoke(tagert, permission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method getPermissionDeniedMethod(Object target) {
        Class<?> targetClass = target.getClass();
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
            APermissionDenied permissionDenied = declaredMethod.getAnnotation(APermissionDenied.class);
            if (permissionDenied != null) {
                return declaredMethod;
            }
        }
        return null;
    }

    private Method getPermissionRationaleMethod(Object target) {
        Class<?> targetClass = target.getClass();
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
            APermissionRationale permissionRationale = declaredMethod.getAnnotation(APermissionRationale.class);
            if (permissionRationale != null) {
                return declaredMethod;
            }
        }
        return null;
    }
}
