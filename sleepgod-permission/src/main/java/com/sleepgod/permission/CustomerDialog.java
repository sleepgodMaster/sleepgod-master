package com.sleepgod.permission;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by cool on 2018/6/27.
 */
public class CustomerDialog extends Dialog {
    public CustomerDialog(@NonNull Context context) {
        this(context, R.style.permission_white_bg_dialog);
    }

    public CustomerDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - Utils.dp2px(context,30);
        window.setAttributes(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public CustomerDialog anim(int animStyleId) {
        Window window = getWindow();
        window.setWindowAnimations(animStyleId);
        return this;
    }

    public CustomerDialog gravity(int gravity) {
        Window window = getWindow();
        window.setGravity(gravity);
        return this;
    }

    public CustomerDialog background(int resid) {
        Window window = getWindow();
        window.setBackgroundDrawableResource(resid);
        return this;
    }

    public CustomerDialog contentView(int layoutId){
        setContentView(layoutId);
        return this;
    }

    public CustomerDialog listeners(View.OnClickListener listener, int ... listenersIds){
        for (int i = 0; i < listenersIds.length; i++) {
            View view = findViewById(listenersIds[i]);
            if(view != null && listener != null){
                view.setOnClickListener(listener);
            }
        }
        return this;
    }

    public CustomerDialog matchWidth(){
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        window.setGravity(Gravity.BOTTOM);
        layoutParams.width = width;
        window.setAttributes(layoutParams);
        return this;
    }
}
