package com.org.sleepgod.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.org.sleepgod.R;


/**
 * 打印自定义Toast(带警告图标)
 * Created by cool on 2016/10/20.
 * 说明：http://blog.csdn.net/cool_fuwei/article/details/52876263
 */

public class AlertToast {
    private volatile static AlertToast mAlertToast;
    private Toast mToast;

    private AlertToast() {

    }

    /**
     * 懒汉单例模式
     * @return
     */
    public static AlertToast getInstance() {
        if (mAlertToast == null) {
            synchronized (AlertToast.class) {
                if(mAlertToast == null) {
                    mAlertToast = new AlertToast();
                }
            }
        }
        return mAlertToast;
    }

    /**
     * 打印Toast
     * @param context
     * @param msg
     */
    public void showToast(Context context, String msg){
        if (mToast == null) {
            synchronized (AlertToast.class) {
                if(mToast == null) {
                    mToast = new Toast(context);
                }
            }
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.item_toast,null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(layout);
        mToast.show();
    }
}
