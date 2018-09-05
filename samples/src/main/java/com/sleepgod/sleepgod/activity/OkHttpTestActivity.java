package com.sleepgod.sleepgod.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cool.butterknife.annoation.BindView;
import com.cool.butterknife.annoation.OnClick;
import com.cool.butterknife.core.ButterKnife;
import com.sleepgod.ok.http.IDownloadCallback;
import com.sleepgod.ok.http.IResponseCallback;
import com.sleepgod.sleepgod.HttpApi;
import com.sleepgod.sleepgod.R;
import com.sleepgod.sleepgod.bean.OkTestBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuiweicai on 2018/6/14.
 */

public class OkHttpTestActivity extends AppCompatActivity {

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

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {

        } else {
            requestPermission(3000, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
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

        params.put("customerId", "198");
        params.put("token", "");
        params.put("complainMessage", "墙掉色");
        params.put("houseId", "459");

        List<File> files = new ArrayList<>();
        files.add(new File("/storage/emulated/0/CHINARES_APP/20180619_0253411.jpg"));
        HttpApi.uploadFile("", files, params,null, new IResponseCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(Object response) {
                resultTv.setText(response.toString());
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
                HttpApi.download("http://a.hiphotos.baidu.com/image/pic/item/c9fcc3cec3fdfc039afa6e3cd83f8794a5c226b7.jpg", "/storage/emulated/0/CHINARES_APP/file_1529134019870.jpg", null, new IDownloadCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long contentLength, int readLength, byte[] bytes) {
                        float progress = readLength * 1.0f / contentLength;
                        final int precent = (int) (progress * 100);
                        Log.e("RunOnUiThread", "onProgress: " + precent);
                        OkHttpTestActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTv.setText(precent + "%");
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
        params.put("city", "北京");

        HttpApi.get("https://www.sojson.com/open/api/weather/json.shtml", params,null, new IResponseCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(Object response) {
                String s = (String) response;
                resultTv.setText(s);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void doPost() {
        Map<String, Object> params = HttpApi.buildRequestParams();
            params.put("page", 1);
            params.put("pageSize", 5);
            params.put("receiverId", "198");


        HttpApi.post("http://rentopsdev.crland.com.cn/api/public/app/message/selectSendMessage", params,OkTestBean.class, new IResponseCallback() {
            @Override
            public void onStart() {

            }


            @Override
            public void onFinish(Object response) {
                OkTestBean bean = (OkTestBean) response;
                resultTv.setText(bean.getData().getSysMessageList().get(0).getContent());
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


}
