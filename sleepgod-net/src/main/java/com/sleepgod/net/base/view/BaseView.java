package com.sleepgod.net.base.view;
/**
 * Created by cool on 2018/6/20.
 */
public interface BaseView {

    /**
     * 显示对话框
     */
    void showToast(String msg);

    /**
     * 显示加载中dialog
     * @param title
     */
    void showLodingDialog(String title);

    /**
     * 隐藏加载中dialog
     */
    void hideLodingDialog();
}
