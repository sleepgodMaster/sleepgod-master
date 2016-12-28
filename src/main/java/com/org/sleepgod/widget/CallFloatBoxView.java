package com.org.sleepgod.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.org.sleepgod.R;

/**
 * 显示悬浮窗口
 * Created by cool on 2016/8/25.
 */
public class CallFloatBoxView {
    private static Context mContext;
    private static View mView;
    private static Boolean isShown = false;
    private static WindowManager wm;

    public static void showFloatBox(Context context) {
        if (isShown) {
            return;
        }
        isShown = true;

        mContext = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.type = type;
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        params.format = PixelFormat.TRANSLUCENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.x = context.getResources().getDisplayMetrics().widthPixels;
        params.y = 0;
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.sese);
        mView = imageView;
        mView.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY;
            int oldOffsetX, oldOffsetY;
            int tag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getX();
                float y = event.getY();
                if (tag == 0) {
                    oldOffsetX = params.x;
                    oldOffsetY = params.y;
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    // 减小偏移量,防止过度抖动
                    params.x += (int) (x - lastX) / 3;
                    params.y += (int) (y - lastY) / 3;
                    tag = 1;
                    if (mView != null)
                        wm.updateViewLayout(mView, params);
                } else if (action == MotionEvent.ACTION_UP) {
                    int newOffsetX = params.x;
                    int newOffsetY = params.y;
                    if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {

                    } else {
                        tag = 0;
                    }
                }
                return true;
            }
        });
        wm.addView(mView, params);
    }

    public static void hideFloatBox() {
        if (isShown && null != mView) {
            wm.removeView(mView);
            isShown = false;
            mView = null;
        }
    }
}
