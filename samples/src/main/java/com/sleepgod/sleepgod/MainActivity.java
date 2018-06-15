package com.sleepgod.sleepgod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sleepgod.sleepgod.Activity.OkHttpTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.center_tv)
    TextView centerTv;
    @BindView(R.id.go_okhttp_bt)
    Button goOkhttpBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.center_tv, R.id.go_okhttp_bt})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.center_tv:
                break;
            case R.id.go_okhttp_bt:
                intent.setClass(this,OkHttpTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
