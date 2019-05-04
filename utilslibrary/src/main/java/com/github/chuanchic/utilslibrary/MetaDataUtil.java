package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * metaData工具类
 */
public class MetaDataUtil {

    /**
     * 获取metaData的value
     */
    public static String getValue(Context context, String name) {
        try {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo info = manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Object value = info.metaData.get(name);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
