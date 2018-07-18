package com.sleepgod.sleepgod.eventbus;

import android.view.View;

import com.cool.butterknife.annoation.OnClick;
import com.cool.butterknife.core.ButterKnife;
import com.cool.tageventbus.EventBus;
import com.sleepgod.net.base.activity.BaseActivity;
import com.sleepgod.sleepgod.R;

public class EventSendActivity extends BaseActivity {

    @Override
    public int setLayoutResID() {
        return R.layout.activity_event_send;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_send_no_args, R.id.bt_send_one_args, R.id.bt_send_two_args,
            R.id.bt_send_three_args, R.id.bt_send_no_args_by_tag, R.id.bt_send_one_args_by_tag, R.id.bt_sync})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send_no_args:
                EventBus.getDefault().post();
                break;
            case R.id.bt_send_one_args:
                EventBus.getDefault().post("张三");
                break;
            case R.id.bt_send_two_args:
                EventBus.getDefault().post("张三",18);
                break;
            case R.id.bt_send_three_args:
                EventBus.getDefault().post("张三",20,new Man("李四",22));
                break;
            case R.id.bt_send_no_args_by_tag:
                EventBus.getDefault().postByTag(EventReceiverActivity.TAG_ABC);
                break;
            case R.id.bt_send_one_args_by_tag:
                EventBus.getDefault().postByTag(EventReceiverActivity.TAG_ABC,"我是事件");
                break;
            case R.id.bt_sync:
                new Thread(){
                    @Override
                    public void run() {
                        EventBus.getDefault().postByTag(EventReceiverActivity.TAG_MAIN);
                    }
                }.start();
                break;
        }
        finish();
    }
}
