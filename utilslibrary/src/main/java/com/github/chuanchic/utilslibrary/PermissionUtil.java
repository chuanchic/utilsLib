package com.github.chuanchic.utilslibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限工具类
 */
public class PermissionUtil {

    /**
     * true Android 6.0及以上，false相反
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取未授予权限的集合
     */
    public static List<String> getDeniedPermissions(String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<String>();
        if(grantResults != null){
            for(int i = 0; i < grantResults.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
        return deniedPermissions;
    }

    /**
     * 获取未授予权限的集合
     */
    public static List<String> getDeniedPermissions(Context context, String... permissions){
        List<String> deniedPermissions = new ArrayList<String>();
        for (String permission : permissions) {
            if(!checkPermission(context, permission)){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 获取targetSdkVersion
     */
    public static int getTargetSdkVersion(Context context){
        int targetSdkVersion = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    /**
     * 检查权限，true允许，false拒绝
     */
    public static boolean checkPermission(Context context, String permission){
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (isOverMarshmallow()) {// >= Android M
            if(Manifest.permission.SYSTEM_ALERT_WINDOW.equals(permission)){
                if (!Settings.canDrawOverlays(context)) {
                    result = false;
                }
            }else if(Manifest.permission.WRITE_SETTINGS.equals(permission)){
                if (!Settings.System.canWrite(context)) {
                    result = false;
                }
            }else{
                int targetSdkVersion = getTargetSdkVersion(context);
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
                    result = context.checkSelfPermission(permission)
                            == PackageManager.PERMISSION_GRANTED;
                } else {
                    // targetSdkVersion < Android M, we have to use PermissionChecker
                    result = PermissionChecker.checkSelfPermission(context, permission)
                            == PermissionChecker.PERMISSION_GRANTED;
                }
            }
        }
        return result;
    }

    /**
     * 检查权限
     * 注意：Specialgrant权限（特殊权限）的申请，需要一个一个的申请，申请的方式是startActivity()的方式
     * 包括：Manifest.permission.SYSTEM_ALERT_WINDOW，
     *      Manifest.permission.WRITE_SETTINGS
     */
    public static boolean checkPermissions(Activity activity, int requestCode, String... permissions){
        List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
        if(deniedPermissions.size() == 0){
            return true;
        }else{
            String[] permissionArr = deniedPermissions.toArray(new String[deniedPermissions.size()]);
            if(Manifest.permission.SYSTEM_ALERT_WINDOW.equals(permissionArr[0])){//Specialgrant权限
                String action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
                String tip = "请允许在其他应用上层显示！";
                requestPermissions(activity, action, tip, requestCode);
            }else if(Manifest.permission.WRITE_SETTINGS.equals(permissionArr[0])){//Specialgrant权限
                String action = Settings.ACTION_MANAGE_WRITE_SETTINGS;
                String tip = "请允许修改系统设置！";
                requestPermissions(activity, action, tip, requestCode);
            }else{
                ActivityCompat.requestPermissions(activity, permissionArr, requestCode);
            }
            return false;
        }
    }

    /**
     * 请求Specialgrant权限
     */
    private static void requestPermissions(Activity activity, String action, String tip, int requestCode){
        Toast.makeText(activity, tip, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(action);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }
}
