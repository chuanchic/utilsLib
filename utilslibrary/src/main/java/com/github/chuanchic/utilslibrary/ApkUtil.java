package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * apk工具类
 */
public class ApkUtil {

    /**
     * 安装apk
     */
    public static void installApk(Context context, String apkPath){
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        if(!file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android N版本或之上
            String authority = context.getPackageName() + ".fileProvider";
            Uri apkUri = FileProvider.getUriForFile(context, authority, file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            Uri apkUri = Uri.fromFile(file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
