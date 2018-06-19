package com.sleepgod.sleepgod.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sleepgod.ok.http.IDownloadCallback;
import com.sleepgod.ok.http.IResponseCallback;
import com.sleepgod.sleepgod.HttpApi;
import com.sleepgod.sleepgod.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        if (checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {

        } else {
            requestPermission(3000,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE);
        }

    }

    public boolean checkPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public void requestPermission(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
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

        HashMap<String, Object> params = new HashMap<>();

        params.put("customerId","198");
        params.put("token","");
        params.put("complainMessage","墙掉色");
        params.put("houseId","459");

        List<File> files = new ArrayList<>();
        files.add(new File("/storage/emulated/0/CHINARES_APP/20180619_0253411.jpg"));
        HttpApi.uploadFile("", files, params, new IResponseCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String response) {
                resultTv.setText(response);
            }

            @Override
            public void onError(Exception e) {
                resultTv.setText(e.getMessage());
            }
        });
    }

    private void doDownload() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpApi.download("http://zd-flat-test-oss.oss-cn-shenzhen.aliyuncs.com/file_1529134019870.jpg", "/storage/emulated/0/CHINARES_APP/file_1529134019870.jpg", null, new IDownloadCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long contentLength, int readLength, byte[] bytes) {
                        float progress =  readLength * 1.0f / contentLength;
                        final int precent = (int) (progress * 100);
                        Log.e("RunOnUiThread", "onProgress: "+precent);
                        OkHttpTestActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTv.setText(precent+"%");
                            }
                        });
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(Exception e) {
                        resultTv.setText(e.getMessage());
                    }
                });
            }
        }).start();

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

        HttpApi.post("", params, new IResponseCallback() {
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
