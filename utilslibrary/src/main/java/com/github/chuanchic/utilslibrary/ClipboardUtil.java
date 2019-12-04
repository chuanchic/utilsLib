package com.github.chuanchic.utilslibrary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 剪贴板工具类
 */
public class ClipboardUtil {

    /**
     * 复制内容到剪贴板
     */
    public static void copyToClipboard(Context context, String content){
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", content);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
