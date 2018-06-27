package com.sleepgod.sleepgod.permission;

import android.Manifest;
import android.util.Log;

import com.sleepgod.permission.annotation.APermission;

/**
 * Created by cool on 2018/6/26.
 */
public class Test {

    //测试授权失败显示的信息
    @APermission(permissions = {Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS},deniedMessage = "授权失败")
    public void test() {
        Log.e("399","有权限,执行某操作");
    }

}
