package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 像素工具类
 */
public class PixelsUtil {
	private static DisplayMetrics displayMetrics;

	public static DisplayMetrics getDisplayMetrics(Context context){
		if(displayMetrics == null){
			displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
		}
		return displayMetrics;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getWidthPixels(Context context){
		int widthPixels = getDisplayMetrics(context).widthPixels;
		int heightPixels = getDisplayMetrics(context).heightPixels;
		return widthPixels < heightPixels ? widthPixels : heightPixels;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getHeightPixels(Context context){
		int widthPixels = getDisplayMetrics(context).widthPixels;
		int heightPixels = getDisplayMetrics(context).heightPixels;
		return widthPixels < heightPixels ? heightPixels : widthPixels;
	}

	/**
	 * 将dip值转换为px值
	 */
	public static int dip2px(Context context, float dpValue) {
		return (int) (dpValue * getDisplayMetrics(context).density + 0.5f);
	}
	
	/**
	 * 将px值转换为dip值
	 */
	public static int px2dip(Context context, float pxValue) {
		return (int) (pxValue / getDisplayMetrics(context).density + 0.5f);
	}
	
	/** 
     * 将sp值转换为px值
     */  
    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getDisplayMetrics(context).scaledDensity + 0.5f);
    }

	/**
	 * 返回值：状态栏的高度，单位：像素
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		Resources resources = context.getResources();
		String name = "status_bar_height";
		int resourceId = resources.getIdentifier(name, "dimen", "android");
		if (resourceId > 0) {
			result = (int) resources.getDimension(resourceId);
		}
		return result;
	}

	/**
	 * 返回值：导航栏的高度,，单位：像素
	 */
	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		Resources resources = context.getResources();
		String name = "navigation_bar_height";
		int resourceId = resources.getIdentifier(name, "dimen", "android");
		if (resourceId > 0) {
			result = (int) resources.getDimension(resourceId);
		}
		return result;
	}
}