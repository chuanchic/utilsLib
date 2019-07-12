package com.github.chuanchic.utilslibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * 软键盘工具类
 */
public class SoftKeyboardUtil {

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideKeyboard(Activity activity) {
        if(activity == null){
            return;
        }
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     * viewList 中需要放的是当前界面所有触发软键盘弹出的控件，
     * 比如一个登陆界面， 有一个账号输入框和一个密码输入框， 需要隐藏键盘的时候，
     * 就将两个输入框对象放在 viewList 中， 作为参数传到 hideSoftKeyboard 方法中即可。
     */
    public static void hideKeyboard(Context context, List<View> viewList) {
        if (context == null || viewList == null || viewList.size() == 0){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View view : viewList) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开或关闭软键盘
     */
    public static void toggleKeyboard(Context context) {
        if(context == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 手动打开软键盘
     * view 为接受软键盘输入的视图，SHOW_FORCED 表示强制显示
     */
    public static void showKeyboard(Context context, View view) {
        if(context == null || view == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 手动关闭软键盘
     * view 为接受软键盘输入的视图，强制关闭
     */
    public static void hideKeyboard(Context context, View view) {
        if(context == null || view == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 获取输入法打开的状态
     * @return 返回true，则表示输入法打开
     */
    public static boolean isActive(Context context){
        if(context == null){
            return false;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

}
