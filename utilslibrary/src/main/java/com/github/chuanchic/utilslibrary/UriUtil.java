package com.github.chuanchic.utilslibrary;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * Uri处理工具
 */
public class UriUtil {

    /**
     * 创建Uri
     * @param file 文件
     */
    public static Uri createUri(Context context, File file){
        if(context == null || file == null || !file.exists()){
            return null;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android N版本或之上
            String authority = context.getPackageName() + ".fileProvider";
            uri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
        }else{
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 解析Uri
     * @param uri 文件路径
     */
    public static String parseUri(Context context, Uri uri){
        if(context == null || uri == null){
            return null;
        }
        String filePath = null;
        if (TextUtils.isEmpty(uri.getAuthority())) {
            filePath = uri.getPath();
        } else {
            ContentResolver resolver = context.getContentResolver();
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = resolver.query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return filePath;
    }

}
