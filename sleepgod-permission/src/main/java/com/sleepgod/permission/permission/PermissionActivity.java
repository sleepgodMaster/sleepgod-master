package com.sleepgod.permission.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sleepgod.permission.CustomerDialog;
import com.sleepgod.permission.R;
import com.sleepgod.permission.RuntimeSettingPage;
import com.sleepgod.permission.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cool on 2018/6/25.
 */
public class PermissionActivity extends Activity {
    public final static int REQUEST_CODE = 0;
    public final static int REQUEST_SETTING = 1;
    public final static String PERMISSIONS = "permission";
    public final static String SHOWRATIONALEDIALOG = "showRationaleDialog";
    private static OnPermissionCallback onPermissionCallback;
    private boolean showRationaleDialog;
    private List<String> mDeniedList;
    private CustomerDialog customerDialog;

    public static void requestPermission(Context context, String[] permissions, boolean showRationaleDialog, OnPermissionCallback callback) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PERMISSIONS, permissions);
        intent.putExtra(SHOWRATIONALEDIALOG, showRationaleDialog);
        onPermissionCallback = callback;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Intent intent = getIntent();
        String[] permissions = intent.getStringArrayExtra(PERMISSIONS);
        showRationaleDialog = intent.getBooleanExtra("SHOWRATIONALEDIALOG", true);
        if(onPermissionCallback == null){
            finish();
            return;
        }
        checkPermissions(permissions);
    }

    private void checkPermissions(String[] permissions) {
        checkPermissionsInManifest(permissions);
        List<String> grantedList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                grantedList.add(permission);
            } else {
                deniedList.add(permission);
            }
        }

        if (grantedList.size() == permissions.length) {
            onPermissionCallback.onGranted();
            finish();
            return;
        }

        ActivityCompat.requestPermissions(this, deniedList.toArray(new String[deniedList.size()]), REQUEST_CODE);
    }

    private void checkPermissionsInManifest(String[] permissions) {
        List<String> manifestPermissions = Utils.getManifestPermissions(this);
        for (String p : permissions) {
            if (!manifestPermissions.contains(p)) {
                throw new IllegalStateException(String.format("The permission %1$s is not registered in manifest.xml", p));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE) return;
        mDeniedList = new ArrayList<>();
        boolean[] granted = getGranted(grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (!granted[i]) {
                mDeniedList.add(permissions[i]);
            }
        }

        if (mDeniedList.size() > 0) {
            List<String> rationaleList = getRationaleList(mDeniedList);
            if (rationaleList.size() > 0) {
                if (showRationaleDialog) {
                    showSettingDialog(rationaleList);
                    return;
                } else {
                    onPermissionCallback.onRationale(rationaleList);
                }
            } else {
                onPermissionCallback.onDenied(mDeniedList);
            }
        } else {
            onPermissionCallback.onGranted();
        }
        finish();
    }

    private boolean[] getGranted(int[] grantResults) {
        boolean[] granted = new boolean[grantResults.length];
        for (int i = 0; i < grantResults.length; i++) {
            granted[i] = grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }
        return granted;
    }

    public List<String> getRationaleList(List<String> deniedList) {
        List<String> rationaleList = new ArrayList<>();
        for (String permission : deniedList) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }

    private void showSettingDialog(final List<String> rationaleList) {
        if(customerDialog == null) {
            customerDialog = new CustomerDialog(this);
            customerDialog.contentView(R.layout.dialog_setting)
                    .listeners(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.tv_cancel) {
                                onPermissionCallback.onRationale(rationaleList);
                                PermissionActivity.this.finish();
                            } else {
                                RuntimeSettingPage runtimeSettingPage = new RuntimeSettingPage(PermissionActivity.this);
                                runtimeSettingPage.start(REQUEST_SETTING);
                                customerDialog.dismiss();
                            }
                        }
                    }, R.id.tv_cancel, R.id.tv_setting);
            TextView textView = customerDialog.findViewById(R.id.tv_message);
            List<String> permissionNames = PermissionTransform.transformText(rationaleList);
            textView.setText(TextUtils.join("\n",permissionNames));
        }
        customerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (onPermissionCallback != null) {
            List<String> deniedList = getDeniedList();
            if (deniedList.size() > 0) {//依然没权限
                List<String> rationaleList = getRationaleList(deniedList);
                if (rationaleList.size() > 0) {
                    onPermissionCallback.onRationale(rationaleList);
                }else {
                    onPermissionCallback.onDenied(deniedList);
                }
            } else {//有权限了
                onPermissionCallback.onGranted();
            }
        }
        finish();
    }

    private List<String> getDeniedList() {
        List<String> deniedList = new ArrayList<>();
        if (mDeniedList != null) {
            for (String permission : mDeniedList) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permission);
                }
            }
        }
        return deniedList;
    }

    public interface OnPermissionCallback {
        void onGranted();

        void onRationale(List<String> roationaleList);

        void onDenied(List<String> deniedList);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        if(customerDialog != null && customerDialog.isShowing()){
            customerDialog.dismiss();
        }
        onPermissionCallback = null;
        super.finish();
    }
}
