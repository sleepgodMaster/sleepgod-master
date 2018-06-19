package com.org.sleepgod.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.org.sleepgod.R;

/**
 * Created by cool on 2016/3/16.
 */
public abstract class BaseFragment extends Fragment{

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(setLayoutResID(), null);
        initView();
        initListeners();
        initData();
        return mView;
    }

    /**
     * 设置布局文件
     *
     * @return 布局文件ID
     */
    public abstract int setLayoutResID();

    protected abstract void initView();


    public void initData() {}

    public void initListeners() {}

    public <T> T findView(int id){
        T view = (T)mView.findViewById(id);
        return view;
    }

    /**
     * 在屏幕中央打印吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
