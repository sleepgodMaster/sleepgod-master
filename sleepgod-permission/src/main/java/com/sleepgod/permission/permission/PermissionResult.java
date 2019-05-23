package com.sleepgod.permission.permission;

import java.util.List;

/**
 * Created by milo on 2019-05-23.
 * @author milo
 */
public class PermissionResult {
    private List<String> grantedList;
    private List<String> deniedList;
    private boolean hasPermission;

    public PermissionResult() {
    }

    public PermissionResult(List<String> grantedList, List<String> deniedList, boolean hasPermission) {
        this.grantedList = grantedList;
        this.deniedList = deniedList;
        this.hasPermission = hasPermission;
    }

    public List<String> getGrantedList() {
        return grantedList;
    }

    public void setGrantedList(List<String> grantedList) {
        this.grantedList = grantedList;
    }

    public List<String> getDeniedList() {
        return deniedList;
    }

    public void setDeniedList(List<String> deniedList) {
        this.deniedList = deniedList;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
}
