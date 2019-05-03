package com.github.chuanchic.utilslibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liuchuanchi on 2016/5/5.
 * 文件存储工具类
 */
public class FileStorageUtil {
    private static String appDir;//App存储路径
    private static String downloadDir;//下载存储路径
    private static String imageDir;//图片存储路径

    /**
     * 获取App存储路径
     */
    public static String getAppDir(Context context, String dir) {
        if (!TextUtils.isEmpty(appDir)) {
            mkdirs(appDir);//创建App存储路径
            return appDir;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            appDir = getExternalAppDir(dir);//外部App存储路径
        } else {
            appDir = getInternalAppCacheDir(context, dir);//内部App缓存路径
        }
        mkdirs(appDir);//创建App存储路径
        return appDir;
    }

    /**
     * 获取下载存储路径
     */
    public static String getDownloadDir(Context context, String dir) {
        if (!TextUtils.isEmpty(downloadDir)) {
            mkdirs(downloadDir);//创建存储路径
            return downloadDir;
        }
        downloadDir = getAppDir(context, dir) + "download/";
        mkdirs(downloadDir);//创建存储路径
        return downloadDir;
    }

    /**
     * 获取图片存储路径
     */
    public static String getImageDir(Context context, String dir) {
        if (!TextUtils.isEmpty(imageDir)) {
            mkdirs(imageDir);//创建存储路径
            return imageDir;
        }
        imageDir = getAppDir(context, dir) + "image/";
        mkdirs(imageDir);//创建存储路径
        return imageDir;
    }

    /**
     * 获取APK下载的存储路径
     */
    public static String getApkDownloadDir(Context context, String dir) {
        String apkDownloadDir = getDownloadDir(context, dir) + "apk/";
        mkdirs(apkDownloadDir);//创建存储路径
        return apkDownloadDir;
    }

    /**
     * 获取内部App缓存路径
     */
    public static String getInternalAppCacheDir(Context context, String dir){
        String packageName = context.getPackageName();//包名
        String dataDirectory = Environment.getDataDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(dataDirectory).append("/data/").append(packageName).append("/").append(dir).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取外部App存储路径
     */
    public static String getExternalAppDir(String dir){
        String externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDir).append("/").append(dir).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取外部App缓存路径
     */
    public static String getExternalAppCacheDir(Context context, String dir) {
        String externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        return getExternalAppCacheDir(context, externalStorageDir, dir);
    }

    /**
     * 获取外部App缓存路径
     */
    public static String getExternalAppCacheDir(Context context, String cacheDir, String dir) {
        String packageName = context.getPackageName();//包名
        StringBuilder sb = new StringBuilder();
        sb.append(cacheDir).append("/Android/data/").append(packageName).append("/")
          .append(dir).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取手机内置，外置的存储根路径集合
     */
    public static String[] getAllStorageDir(Context context) {
        try {
            StorageManager manager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
            Method method = manager.getClass().getMethod("getVolumePaths");
            return (String[]) method.invoke(manager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建目录
     */
    public static void mkdirs(String filePath){
        File dirFile = new File(filePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();//mkdir()不能创建多个目录，所以要用mkdirs()
        }
    }

    public static File getImage(Context context, String dir){
        String imageName = System.currentTimeMillis() + ".png";
        return new File(getImageDir(context, dir), imageName);
    }
}
