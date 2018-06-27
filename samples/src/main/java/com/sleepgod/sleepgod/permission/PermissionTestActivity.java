package com.sleepgod.sleepgod.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sleepgod.permission.Permission;
import com.sleepgod.permission.annotation.APermission;
import com.sleepgod.permission.annotation.APermissionDenied;
import com.sleepgod.permission.annotation.APermissionRationale;
import com.sleepgod.sleepgod.R;

public class PermissionTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_test);
    }

    @APermission(permissions = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION},deniedMessage = "授权失败")
    public void testActivity(View view) {
        Toast.makeText(this,"授权成功,执行接下来的代码",Toast.LENGTH_SHORT).show();
    }


    @APermission(permissions = {Manifest.permission.BODY_SENSORS,Manifest.permission.READ_CALENDAR},requestCode = 10)
    public void testRequestCode(View view) {
        Toast.makeText(this,"授权成功,执行接下来的代码",Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试任意类申请权限
     * @param view
     */
    public void testNormal(View view) {
        Test test = new Test();
        test.test();
    }

    @APermissionDenied
    public void permissionDenied(Permission permission){
        Log.e("399","permissionDenied");
        Log.e("399","code: " +permission.getRequestCode());
        Log.e("399","list: " +permission.getRequestPermissions());
        Toast.makeText(this,"permissionDenied " + "code: " +permission.getRequestCode() + "list: " +permission.getRequestPermissions(),Toast.LENGTH_SHORT).show();
    }

    @APermissionRationale
    public void permissionRationale(Permission permission){
        Log.e("399","permissionRationale");
        Log.e("399","code: " +permission.getRequestCode());
        Log.e("399","list: " +permission.getRequestPermissions());
        Toast.makeText(this,"permissionRationale " + "code: " +permission.getRequestCode() + " list: " +permission.getRequestPermissions(),Toast.LENGTH_SHORT).show();

    }

}
