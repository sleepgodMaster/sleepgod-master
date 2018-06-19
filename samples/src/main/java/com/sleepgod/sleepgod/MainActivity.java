package com.sleepgod.sleepgod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sleepgod.sleepgod.activity.OkHttpTestActivity;
import com.sleepgod.sleepgod.activity.RetrofitTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_retrofit, R.id.go_okhttp_bt})
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
        }
    }
}
