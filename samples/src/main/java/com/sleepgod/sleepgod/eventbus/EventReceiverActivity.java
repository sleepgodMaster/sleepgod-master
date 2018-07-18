package com.sleepgod.sleepgod.eventbus;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cool.butterknife.annoation.BindView;
import com.cool.butterknife.annoation.OnClick;
import com.cool.butterknife.core.ButterKnife;
import com.cool.tageventbus.EventBus;
import com.cool.tageventbus.Subscribe;
import com.cool.tageventbus.ThreadMode;
import com.sleepgod.net.base.activity.BaseActivity;
import com.sleepgod.sleepgod.R;

public class EventReceiverActivity extends BaseActivity {
    public final static String TAG_ABC = "abc";
    public final static String TAG_MAIN = "mian";
    @BindView(R.id.tv_message)
    TextView mMessageTextView;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_event_receiver;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onReceiver() {
        mMessageTextView.setText("接收到无参事件");
    }

    @Subscribe
    public void onReceiver(String name) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到1个参数的事件\n");
        stringBuffer.append("name=" + name + "\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe
    public void onReceiver(String name, int age) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到2个参数的事件\n");
        stringBuffer.append("name=" + name + "\n");
        stringBuffer.append("age=" + age + "\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe
    public void onReceiver(String name, int age, Man man) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到3个参数的事件\n");
        stringBuffer.append("name=" + name + "\n");
        stringBuffer.append("age=" + age + "\n");
        stringBuffer.append("man=" + man.toString() + "\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe
    public void onReceiverStickyEvent(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到1个参数的粘性事件,也就是在注册前就发送了的事件\n");
        stringBuffer.append("msg=" + msg + "\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe(tag = TAG_ABC)
    public void onReceiverByTag() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到无参数的事件,指定接收的Tag\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe(tag = TAG_ABC)
    public void onReceiverByTag(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到1个参数的事件,指定接收的Tag\n");
        stringBuffer.append("msg=" + msg + "\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Subscribe(tag = TAG_MAIN, threadMode = ThreadMode.MAIN)
    public void onReceiverOnAsync() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接收到无参数的事件,事件在子线程中发送,接收的线程为主线程\n");
        mMessageTextView.setText(stringBuffer.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.bt_send)
    public void onClick(View view) {
        startActivity(new Intent(this, EventSendActivity.class));
    }
}
