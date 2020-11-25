package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 文件存储工具类
 */
public class FileStorageUtil {
    private static String cacheAppDir;//App存储路径
    private static String cacheDownloadDir;//下载存储路径
    private static String cacheImageDir;//图片存储路径

    /**
     * 创建目录
     * @param filePath 文件路径
     */
    public static void mkdirs(String filePath){
        File dirFile = new File(filePath);
        if(!dirFile.exists()){
            //mkdir()不能创建多个目录，要用mkdirs()
            dirFile.mkdirs();
        }
    }

    /**
     * 获取外部App存储路径
     * @param context 上下文
     * @param appDir app根路径
     */
    public static String getExternalAppDir(Context context, String appDir){
        String appPath = new StringBuilder()
                .append(context.getExternalFilesDir(null).getAbsolutePath()).append("/")
                .append(appDir).append("/")
                .toString();
        mkdirs(appPath);
        return appPath;
    }

    /**
     * 获取内部App存储路径
     * @param context 上下文
     * @param appDir app根路径
     */
    public static String getInternalAppDir(Context context, String appDir){
        String appPath = new StringBuilder()
                .append(Environment.getDataDirectory().getAbsolutePath())
                .append("/data/")
                .append(context.getPackageName()).append("/")
                .append(appDir).append("/")
                .toString();
        mkdirs(appPath);
        return appPath;
    }

    /**
     * 获取App存储路径
     * @param context 上下文
     * @param appDir app根路径
     */
    public static String getAppDir(Context context, String appDir) {
        if (TextUtils.isEmpty(cacheAppDir)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                cacheAppDir = getExternalAppDir(context, appDir);
            } else {
                cacheAppDir = getInternalAppDir(context, appDir);
            }
        }
        mkdirs(cacheAppDir);
        return cacheAppDir;
    }

    /**
     * 获取下载存储路径
     * @param context 上下文
     * @param appDir app根路径
     */
    public static String getDownloadDir(Context context, String appDir) {
        if (TextUtils.isEmpty(cacheDownloadDir)) {
            cacheDownloadDir = getAppDir(context, appDir) + "download/";
        }
        mkdirs(cacheDownloadDir);
        return cacheDownloadDir;
    }

    /**
     * 获取图片存储路径
     * @param context 上下文
     * @param appDir app根路径
     */
    public static String getImageDir(Context context, String appDir) {
        if (TextUtils.isEmpty(cacheImageDir)) {
            cacheImageDir = getAppDir(context, appDir) + "image/";
        }
        mkdirs(cacheImageDir);
        return cacheImageDir;
    }

    /**
     * 获取图片
     * @param context 上下文
     * @param appDir app根路径
     */
    public static File getImage(Context context, String appDir){
        String imageDir = getImageDir(context, appDir);
        String imageName = System.currentTimeMillis() + ".png";
        return new File(imageDir, imageName);
    }

}
