package com.sleepgod.permission;

import java.util.List;

/**
 * Created by cool on 2018/6/27.
 */
public class Permission {
    private List<String> requestPermissions;
    private int requestCode;

    public Permission(List<String> requestPermissions, int requestCode) {
        this.requestPermissions = requestPermissions;
        this.requestCode = requestCode;
    }

    public List<String> getRequestPermissions() {
        return requestPermissions;
    }

    public void setRequestPermissions(List<String> requestPermissions) {
        this.requestPermissions = requestPermissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
