package com.github.chuanchic.utilslibrary;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

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
     * @param context 上下文
     * @param uri 文件uri
     * @return 文件路径
     */
    public static String parseUri(Context context, Uri uri){
        if(context == null || uri == null){
            return null;
        }
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是 document 类型的 uri, 则通过 document id 来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                final String[] split = documentId.split(":");
                final String type = split[0];
                final String id = split[1];

                String[] selectionArgs = { id };
                String selection = null;
                Uri contentUri = null;
                if ("image".equals(type)) {
                    selection = MediaStore.Images.Media._ID + "=?";
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    selection = MediaStore.Video.Media._ID + "=?";
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    selection = MediaStore.Audio.Media._ID + "=?";
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                try {
                    long id = Long.parseLong(documentId);
                    String uriString = "content://downloads/public_downloads";
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(uriString), id);
                    filePath = getDataColumn(context, contentUri, null, null);
                }catch (Exception e){
                }
            } else if (isExternalStorageDocument(uri)) {
                final String[] split = documentId.split(":");
                final String type = split[0];
                final String id = split[1];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + id;
                }
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 file 类型的 Uri, 直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        if(context == null || uri == null){
            return null;
        }
        Cursor cursor = null;
        try {
            String column = "_data";
            String[] proj = { column };
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, proj, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(column));
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    public static boolean isMediaDocument(Uri uri) {
        if(uri == null){
            return false;
        }
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    public static boolean isDownloadsDocument(Uri uri) {
        if(uri == null){
            return false;
        }
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

}
