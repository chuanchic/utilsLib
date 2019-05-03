package com.github.chuanchic.utilslibrary.threadutil;

/**
 * 单线程代理（文件用）
 */
public final class FileProxy extends SingleThreadProxy {

    private FileProxy(){
        super();
    }

    private static final class LazyHolder {
        private static final FileProxy INSTANCE = new FileProxy();//单例
    }

    public static FileProxy getInstance(){
        return LazyHolder.INSTANCE;
    }
}
