package com.sleepgod.sleepgod.permission;

import com.sleepgod.permission.permission.Permission;

/**
 * Created by cool on 2018/7/11.
 */
public interface IPermissionTestActivity {
    void permissionDenied(Permission permission);

    void permissionRationale(Permission permission);
}
