package com.sleepgod.net.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by cool on 2016/3/13.
 */
public abstract class BaseActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());//设置布局文件
        fillData();
        initView();
        initListeners();
        initData();
    }


    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    /**
     * 初始化Listener,需要实现是覆盖
     */
    public void initListeners() {

    }

    public void fillData() {

    }

    /**
     * 初始化view,必须实现
     */
    public abstract void initView();

    /**
     * 设置布局文件
     *
     * @return 布局文件ID
     */
    public abstract int setLayoutResID();


    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 打印吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示加载进度条
     *
     * @param title
     */
    public void showLodingDialog(String title) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideLodingDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
