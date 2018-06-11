package com.org.sleepgod.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.org.sleepgod.R;

/**
 * Created by cool on 2016/3/13.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());//设置布局文件

        //为所有按钮设置点击事件
        View rootView = findViewById(android.R.id.content);// android.R.id.content这个id可以获取到Activity的根View
        //寻找按钮组件，并设置点击事件
        findButtonAndSetOnClickListener(rootView);

        initView();
        initListeners();
        initData();
    }



    /**
     * 寻找按钮组件，并设置点击事件，子类如果需要响应按钮点击事件直接实现本类中的onClick(View v, int id)方法
     * @param rootView
     */
    private void findButtonAndSetOnClickListener(View rootView) {
        if(rootView instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) rootView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if(child instanceof Button || child instanceof ImageButton){
                    child.setOnClickListener(this);
                }else if(child instanceof ViewGroup){
                    findButtonAndSetOnClickListener(child);
                }
            }
        }
    }

    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id){
        T view = (T)findViewById(id);
        return view;
    }

    /**
     * 初始化Listener,需要实现是覆盖
     */
    public void initListeners() {

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
     * 在屏幕中央打印吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
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
//                finish();
//                break;

            default://其他的情况下让子类自己处理

                onClick(v, v.getId());

                break;
        }
    }
}
