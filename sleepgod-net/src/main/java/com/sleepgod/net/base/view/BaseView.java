package com.sleepgod.net.base.view;

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
