package com.github.chuanchic.utilslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;

/**
 * 文件通用工具类
 */
@SuppressLint("NewApi")
public class FileCommonUtil {

    public static final String filePrefix = "file://";//文件前缀
    public static final int ruleFileLastModify = 0;//文件上次修改时间排序
    public static final int ruleFileName = 1;//文件名排序
    public static final int ruleFileSize = 2;//文件大小排序

    /**
     * 写文件
     */
    public static void writeFileSerializable(Serializable serializable, String path) {
        if(serializable == null || TextUtils.isEmpty(path)) {
            return;
        }
        writeFileSerializable(serializable, new File(path));
    }

    /**
     * 写文件
     */
    public static void writeFileSerializable(Serializable serializable, File file) {
        if(serializable == null || file == null) {
            return;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serializable);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                oos.close();
            }catch (Exception e){
            }
            try {
                fos.close();
            }catch (Exception e){
            }
        }
    }

    /**
     * 读文件
     */
    public static Serializable readFileSerializable(String path) {
        if(TextUtils.isEmpty(path)) {
            return null;
        }
        return readFileSerializable(new File(path));
    }

    /**
     * 读文件
     */
    public static Serializable readFileSerializable(File file) {
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Serializable serializable = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            serializable = (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ois.close();
            }catch (Exception e){
            }
            try {
                fis.close();
            }catch (Exception e){
            }
        }
        return serializable;
    }

    /**
     * 写文件
     */
    public static void writeFileString(String jsonStr, String path) {
        if(TextUtils.isEmpty(path)) {
            return;
        }
        writeFileString(jsonStr, new File(path));
    }

    /**
     * 写文件
     */
    public static void writeFileString(String jsonStr, File file) {
        if(file == null){
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(jsonStr);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                osw.close();
            }catch (Exception e){
            }
            try {
                fos.close();
            }catch (Exception e){
            }
        }
    }

    /**
     * 读文件
     */
    public static String readFileString(String path) {
        if(TextUtils.isEmpty(path)) {
            return null;
        }
        return readFileString(new File(path));
    }

    /**
     * 读文件
     */
    public static String readFileString(File file) {
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = new FileInputStream(file);
            isr = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            }catch (Exception e){
            }
            try {
                isr.close();
            }catch (Exception e){
            }
            try {
                inputStream.close();
            }catch (Exception e){
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 写文件
     */
    public static void writeFileBytes(byte[] bytes, File file) {
        if(bytes == null || file == null){
            return;
        }
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            output = new FileOutputStream(file);
            outBuff = new BufferedOutputStream(output);
            outBuff.write(bytes);
            outBuff.flush();// 刷新此缓冲的输出流
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outBuff.close();
            }catch (Exception e){
            }
            try {
                output.close();
            }catch (Exception e){
            }
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return false;
        }
        return deleteFile(new File(filePath));
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File curFile) {
        if(curFile == null || !curFile.exists()){
            return false;
        }
        if (curFile.isDirectory()) {//删除子目录
            File[] childrenFile = curFile.listFiles();
            if (childrenFile != null && childrenFile.length > 0) {
                for (int i = 0; i < childrenFile.length; i++) {
                    deleteFile(childrenFile[i]);
                }
            }
        }
        return curFile.delete();//删除当前file
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) {
        if(!TextUtils.isEmpty(sourceFilePath) && !TextUtils.isEmpty(targetFilePath)){
            copyFile(new File(sourceFilePath), new File(targetFilePath), 1024 * 5);
        }
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(File sourceFile, File targetFile, int buffSize) {
        if(sourceFile == null || targetFile == null){
            return;
        }
        if(!sourceFile.exists()){
            return;
        }
        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            input = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(input);
            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);

            byte[] bytes = new byte[buffSize];// 缓冲数组
            int len;
            while ((len = inBuff.read(bytes)) != -1) {
                outBuff.write(bytes, 0, len);
            }
            outBuff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                outBuff.close();
            }catch (Exception e){
            }
            try {
                output.close();
            }catch (Exception e){
            }
            try {
                inBuff.close();
            }catch (Exception e){
            }
            try {
                input.close();
            }catch (Exception e){
            }
        }
    }

    /**
     * 拷贝文件（使用shell命令）
     */
    public static boolean copyFileByShell(String oldPath, String newPath) {
        boolean copyResult = false;
        Process proc = null;
        try {
            String cmd = "cp -r " + oldPath + "/. " + newPath;
            proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() == 0) {
                copyResult = true;
            }else{
                printShellError(proc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(proc != null){
                proc.destroy();
                proc = null;
            }
        }
        return copyResult;
    }

    /**
     * 打印失败原因
     */
    public static void printShellError(Process proc) {
        try {
            InputStream in = proc.getErrorStream();
            StringBuilder result = new StringBuilder();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                 result.append(new String(re));
            }
        } catch (IOException e) {
        }
    }

    /**
     * 重命名文件
     */
    public static boolean renameFile(String path, String newName) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            String dir = file.getParentFile().getAbsolutePath();
            File newFile = new File(dir, newName);
            return file.renameTo(newFile);
        }
        return false;
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileNameSuffix(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(lastIndex);
            }
        }
        return null;
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileNameSuffixNoDot(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(lastIndex + 1);
            }
        }
        return null;
    }

    /**
     * 获取文件名
     */
    public static String getFileNameNoSuffix(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(0, lastIndex);
            }
        }
        return null;
    }

    /**
     * 清除app缓存
     */
    public static void cleanAppCache(final Context context, final TextView tv_cache_size) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                cleanAppCache(context);//清除app缓存
                return FileSizeUtil.getAppCacheSize(context);
            }

            @Override
            protected void onPostExecute(String appCacheSize) {
                super.onPostExecute(appCacheSize);
                if(tv_cache_size != null){
                    tv_cache_size.setText(appCacheSize);
                }
            }
        }.execute();
    }

    /**
     * 清除app缓存
     */
    private static void cleanAppCache(Context context){
        if(context == null){
            return;
        }

        //删除内部数据
        deleteFile(context.getCacheDir());
        deleteFile(context.getFilesDir());
        deleteFile(new File("/data/data/" + context.getPackageName() + "/databases"));
//        deleteFile(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));

        //删除外部数据
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            deleteFile(context.getExternalCacheDir());
            deleteFile(context.getExternalFilesDir(null));
        }
    }

    /**
     * 获取真正的视频地址
     * @param activity
     * @param path content://media/external/video/media/1711
     * @return 返回 /storage/emulated/0/阿波罗11号3D.mp4
     */
    public static String getRealVideoPath(Activity activity, String path){
        if(activity == null || TextUtils.isEmpty(path)){
            return path;
        }else if(path.startsWith("content://")){
            String[] proj = { MediaStore.Video.Media.DATA };
            Cursor cursor = activity.managedQuery(Uri.parse(path), proj, null, null, null);
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                String realPath = cursor.getString(column_index);

                if(Build.VERSION.SDK_INT < 14) { //4.0及其以上的版本中，Cursor会自动关闭
                    cursor.close();
                }
                return realPath;
            }else{
                return path;
            }
        }else{
            return path;
        }
    }

    /**
     * 扫描apk文件
     */
    public static void scanApks(File file, List<File> apkList){
        if(file != null && file.exists()){
            if(file.isDirectory()){//文件夹
                File[] childrenFiles = file.listFiles();
                if (childrenFiles != null && childrenFiles.length > 0) {
                    for (int i = 0; i < childrenFiles.length; i++) {
                        scanApks(childrenFiles[i], apkList);
                    }
                }
            }else{//文件
                if(file.getAbsolutePath().endsWith(".apk")){
                    apkList.add(file);
                }
            }
        }
    }

}
