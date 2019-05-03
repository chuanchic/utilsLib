package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件大小工具类
 */
public class FileSizeUtil {

	public static final double TB = 1024 * 1024 * 1024 * 1024f;
	public static final double GB = 1024 * 1024 * 1024f;
	public static final double MB = 1024 * 1024f;
	public static final double KB = 1024f;

	/**
	 * 获取缓存大小
	 */
	public static String getAppCacheSize(Context context){
		long cacheDirSize = getFileSize(context.getCacheDir());
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cacheDirSize += getFileSize(context.getExternalCacheDir());
		}
		return formatFileSize(cacheDirSize);
	}

	/**
	 * 获取文件大小
	 */
	public static long getFileSize(String filePath) {
		if(TextUtils.isEmpty(filePath)){
			return 0;
		}
		return getFileSize(new File(filePath));
	}
	
	/**
	 * 获取文件大小
	 */
    public static long getFileSize(File file) {
		long size = 0;
		if(file != null && file.exists()){
			if(file.isFile()){
				size = file.length();
			}else if(file.isDirectory()) {
				File[] fileList = file.listFiles();
				if(fileList != null && fileList.length > 0){
					for (int i = 0; i < fileList.length; i++) {
						size += getFileSize(fileList[i]);
					}
				}
			}
		}
		return size;
    }

	/**
	 * 格式化文件大小
	 */
	public static String formatFileSize(long diskSize) {
		DecimalFormat df = new DecimalFormat("0.00");
		String res;
		double tempSize = (double) diskSize;

		if (tempSize >= TB) {
			res = df.format(tempSize / TB) + "TB";
		} else if (tempSize >= GB) {
			res = df.format(tempSize / GB) + "GB";
		} else if (tempSize >= MB) {
			res = df.format(tempSize / MB) + "MB";
		} else if (tempSize >= KB) {
			res = df.format(tempSize / KB) + "KB";
		} else {
			res = diskSize + "B";
		}
		if(res.contains(".00")){
			res = res.replace(".00","");
		}

		return res;
	}

	/**
	 * 格式化文件大小
	 */
	public static String formatFileSize2(long diskSize) {
		DecimalFormat df = new DecimalFormat("0.00");
		String res;
		double tempSize = (double) diskSize;

		if (tempSize >= TB) {
			res = df.format(tempSize / TB) + "TB";
		} else if (tempSize >= GB) {
			res = df.format(tempSize / GB) + "GB";
		} else if(tempSize >= MB * 10){//大于10MB，省略小数点后几位
			res = new DecimalFormat("0").format(tempSize / MB) + "MB";
		} else if (tempSize >= MB) {
			res = df.format(tempSize / MB) + "MB";
		} else if (tempSize >= KB) {
			res = df.format(tempSize / KB) + "KB";
		} else {
			res = diskSize + "B";
		}
		if(res.contains(".00")){
			res = res.replace(".00", "");
		}

		return res;
	}

	public static float getSizeFromString(String size){
		if(size == null){
			return -1;
		}
		float finlySize = -1f;
		try {
			if (size.endsWith("KB")) {
				finlySize = Float.parseFloat(size.substring(0, size.indexOf("KB"))) * 1024L;
			} else if (size.endsWith("MB")) {
				finlySize = Float.parseFloat(size.substring(0, size.indexOf("MB"))) * 1024L * 1024;
			} else if (size.endsWith("GB")) {
				finlySize = Float.parseFloat(size.substring(0, size.indexOf("GB"))) * 1024L * 1024 * 1024;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return finlySize;
	}
}
