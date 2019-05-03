package com.github.chuanchic.utilslibrary.threadutil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {
	private ExecutorService cachedThreadPool = null;

	private ThreadPoolUtil(){
		initCachedThreadPool();
	}

	private void initCachedThreadPool(){
		if (cachedThreadPool == null) {
			cachedThreadPool = Executors.newCachedThreadPool();
		}
	}

	private static final class LazyHolder {
		private static final ThreadPoolUtil INSTANCE = new ThreadPoolUtil();//单例
	}

	public static ThreadPoolUtil getInstance(){
		return LazyHolder.INSTANCE;
	}

	public void runThread(Runnable runnable) {
		initCachedThreadPool();
		cachedThreadPool.execute(runnable);
	}

	public void clearExecutorService() {
		if (cachedThreadPool != null) {
			cachedThreadPool.shutdownNow();
			cachedThreadPool = null;
		}
	}
}
