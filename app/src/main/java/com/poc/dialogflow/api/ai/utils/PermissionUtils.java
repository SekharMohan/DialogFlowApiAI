package com.poc.dialogflow.api.ai.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static boolean hasMashMallow() {
        return VERSION.SDK_INT >= VERSION_CODES.M;
    }

    public static void requestPermissions(Activity activity,
                                          String[] permissions,
                                          int requestCode) {
        if (PermissionUtils.hasMashMallow()
                && PermissionUtils.checkManifestPermission(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public static boolean checkManifestPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldShownPermissionRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermission(String permission, Activity mActivity) {
        return ActivityCompat.checkSelfPermission(mActivity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermission(String[] permissionsList, Activity mActivity) {
        for (String permission : permissionsList) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //this will ask user to allow particular permission by showing prompt, if you are running marshmallow OS.
    public static void requestPermission(String permission, int requestCode, Activity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
        }
    }
}
