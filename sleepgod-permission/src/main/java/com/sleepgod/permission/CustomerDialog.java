package com.sleepgod.permission;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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
    }

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

    public CustomerDialog background(Drawable drawable){
        Window window = getWindow();
        window.setBackgroundDrawable(drawable);
        return this;
    }

    public CustomerDialog contentView(int layoutId) {
        setContentView(layoutId);
        return this;
    }

    public CustomerDialog text(int id, String text) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    public CustomerDialog visible(int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    public CustomerDialog cancelable(boolean cancelable){
        setCancelable(cancelable);
        return this;
    }

    public CustomerDialog listeners(View.OnClickListener listener, int... listenersIds) {
        for (int i = 0; i < listenersIds.length; i++) {
            View view = findViewById(listenersIds[i]);
            if (view != null && listener != null) {
                view.setOnClickListener(listener);
            }
        }
        return this;
    }

    public CustomerDialog matchWidth() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        window.setGravity(Gravity.BOTTOM);
        layoutParams.width = width;
        window.setAttributes(layoutParams);
        return this;
    }

    public CustomerDialog defaultMargin(){
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels - PermissionUtils.dp2px(getContext(), 30);
        window.setAttributes(layoutParams);
        return this;
    }

    public CustomerDialog customerMargin(int dp){
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels - PermissionUtils.dp2px(getContext(), dp);
        window.setAttributes(layoutParams);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }
}
