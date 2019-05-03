package com.github.chuanchic.utilslibrary.threadutil;

/**
 * 单线程代理（数据库用）
 */
public final class SqliteProxy extends SingleThreadProxy {

    private SqliteProxy(){
        super();
    }

    private static final class LazyHolder {
        private static final SqliteProxy INSTANCE = new SqliteProxy();//单例
    }

    public static SqliteProxy getInstance(){
        return LazyHolder.INSTANCE;
    }
}
