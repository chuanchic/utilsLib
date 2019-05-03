package com.github.chuanchic.utilslibrary;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * asset工具类
 */
public class AssetUtil {

    /**
     * 加载asset文件夹里的文件
     * @param fileName 文件名
     * @return 字符串
     */
    public static String loadAssetFileAsString(Context context, String fileName) {
        StringBuilder buf = new StringBuilder();

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            boolean isFirst = true;
            while ( (str = bufferedReader.readLine()) != null ) {
                if (isFirst){
                    isFirst = false;
                }else{
                    buf.append('\n');
                }
                buf.append(str);
            }
        } catch (IOException e) {
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
            }
            try {
                inputStreamReader.close();
            } catch (IOException e) {
            }
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }

        return buf.toString();
    }

}
