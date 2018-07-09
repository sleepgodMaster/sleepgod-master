package com.sleepgod.sleepgod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cool.butterknife.annoation.OnClick;
import com.cool.butterknife.core.ButterKnife;
import com.sleepgod.sleepgod.activity.OkHttpTestActivity;
import com.sleepgod.sleepgod.activity.RetrofitTestActivity;
import com.sleepgod.sleepgod.permission.PermissionTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_retrofit, R.id.go_okhttp_bt,R.id.bt_aop})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.bt_retrofit:
                intent.setClass(this, RetrofitTestActivity.class);
                startActivity(intent);
                break;
            case R.id.go_okhttp_bt:
                intent.setClass(this,OkHttpTestActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_aop:
                intent.setClass(this, PermissionTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
