package com.github.chuanchic.utilslibrary.threadutil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 单线程代理
 */
public abstract class SingleThreadProxy {

    private ExecutorService singleThreadExecutor;//单线程池

    protected SingleThreadProxy(){
        initExecutorService();
    }

    protected void initExecutorService(){
        try {
            if (singleThreadExecutor == null) {
                singleThreadExecutor = Executors.newSingleThreadExecutor();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearExecutorService() {
        try {
            if (singleThreadExecutor != null) {
                singleThreadExecutor.shutdown();
                singleThreadExecutor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProxyRunnable(final ProxyRunnable proxyRunnable) {
        initExecutorService();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    proxyRunnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public <T> T addProxyExecute(final ProxyExecute<T> proxyExecute) {
        initExecutorService();
        try {
            Future<T> future = singleThreadExecutor.submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    T object = proxyExecute.execute();
                    return object;
                }
            });
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ProxyRunnable {
        void run();
    }

    public interface ProxyExecute<T> {
        T execute();
    }
}
