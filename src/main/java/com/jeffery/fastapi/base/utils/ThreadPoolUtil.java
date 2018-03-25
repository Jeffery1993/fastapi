package com.jeffery.fastapi.base.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);

	public static ExecutorService getThreadPool() {
		return fixedThreadPool;
	}

}
