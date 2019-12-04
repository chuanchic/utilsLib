package com.github.chuanchic.utilslibrary;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * md5加密
 */
public class MD5Util {

    /**
     * 加密文件
     */
    public static String md5(File file) {
        String result = "";
        if (file != null && file.isFile() && file.exists()) {
            FileInputStream fileInputStream = null;
            byte buffer[] = new byte[8192];
            int len;
            try {
                fileInputStream = new FileInputStream(file);
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                while ((len = fileInputStream.read(buffer)) != -1) {
                    messageDigest.update(buffer, 0, len);
                }
                byte[] bytes = messageDigest.digest();

                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    /**
     * 加密字符串
     */
    public static String md5(String string) {
        String result = "";
        if (!TextUtils.isEmpty(string)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] bytes = messageDigest.digest(string.getBytes());

                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
