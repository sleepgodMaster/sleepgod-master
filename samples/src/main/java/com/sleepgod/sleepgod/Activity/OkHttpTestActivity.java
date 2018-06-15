package com.sleepgod.sleepgod.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.sleepgod.ok.http.IResponseCallback;
import com.sleepgod.sleepgod.HttpApi;
import com.sleepgod.sleepgod.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cuiweicai on 2018/6/14.
 */

public class OkHttpTestActivity extends FragmentActivity {

    @BindView(R.id.post_tv)
    TextView postTv;
    @BindView(R.id.get_tv)
    TextView getTv;
    @BindView(R.id.download_tv)
    TextView downloadTv;
    @BindView(R.id.upload_tv)
    TextView uploadTv;
    @BindView(R.id.result_tv)
    TextView resultTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_test);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.post_tv, R.id.get_tv, R.id.download_tv, R.id.upload_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_tv:
                doPost();
                break;
            case R.id.get_tv:
                doGet();
                break;
            case R.id.download_tv:
                doDownload();
                break;
            case R.id.upload_tv:
                doUpload();
                break;
        }
    }

    private void doUpload() {

    }

    private void doDownload() {

    }

    private void doGet() {
        Map<String, Object> params = new HashMap<>();
        params.put("city","北京");

        HttpApi.get("https://www.sojson.com/open/api/weather/json.shtml", params, new IResponseCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String response) {
                resultTv.setText(response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void doPost() {
        JSONObject params = HttpApi.buildRequestParams();
        try {
            params.put("page", 1);
            params.put("pageSize", 1);
            params.put("hotFlag", "Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpApi.post("http://rentopsdev.crland.com.cn/crlandrentoperate/api/public/app/house/query", params, new IResponseCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String response) {
                resultTv.setText(response);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }



}
