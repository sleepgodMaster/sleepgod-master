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
public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(setLayoutResID(), null);
        initView();
        initListener();
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

    public void initListener() {}

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
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 子类实现此方法处理点击事件
     * @param v
     * @param id
     */
    public void onClick(View v, int id){}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //因为项目中都会返回的这个按钮，在ids.xml中创建返回按钮的id,项目中用到直接引用，点击返回功能此处已经完成
//            case R.id.btn_back://点击id为btn_back的按钮统一处理
//                getActivity().finish();
//                break;

            default://其他的情况下让子类自己处理

                onClick(v, v.getId());

                break;
        }
    }
}
