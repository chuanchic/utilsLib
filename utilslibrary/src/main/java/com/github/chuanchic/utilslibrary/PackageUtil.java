package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;

/**
 * 包工具类
 */
public class PackageUtil {
    /**
     * 获取包信息
     */
    public static PackageInfo getPackageArchiveInfo(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取包信息
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    public static int getVersionCodeByFile(Context context, File file) {
        PackageInfo packageInfo = getPackageArchiveInfo(context, file.getAbsolutePath());
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }
}
