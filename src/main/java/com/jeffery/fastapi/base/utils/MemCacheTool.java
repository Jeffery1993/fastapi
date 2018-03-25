package com.jeffery.fastapi.base.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCacheTool {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private LinkedHashMap<String, Node> cache = new LinkedHashMap<String, Node>();
	private long cacheTime = 900L;
	private int maxSize = 100;
	private int gcSize = 10;
	private long lastGcTime = System.currentTimeMillis();
	private long gcInterval = 10000L;

	MemCacheTool() {

	}

	MemCacheTool(long cacheTime, int gcSize) {
		this.cacheTime = cacheTime;
		this.gcSize = gcSize;
	}

	public void put(String key, Object value) {
		long _now = System.currentTimeMillis();
		cache.put(key, new Node(_now, value));
		if (cache.size() > gcSize && _now > lastGcTime + gcInterval) {
			gc();
		}
		if (cache.size() > maxSize) {
			remove(cache.size() - maxSize);
		}
	}

	public synchronized void remove(String key) {
		cache.remove(key);
	}

	private void remove(int size) {
		List<String> removeKeys = new ArrayList<String>();
		int index = 0;
		for (Iterator<String> iterator = cache.keySet().iterator(); iterator.hasNext() && index < size; index++) {
			String key = iterator.next();
			removeKeys.add(key);
		}
		for (String key : removeKeys) {
			cache.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(String key, Class<T> classType) {
		try {
			Object res = get(key);
			return (res == null) ? null : (T) res;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Object get(String key) {
		Node node = cache.get(key);
		if (cache == null) {
			return null;
		}
		long now = new Date().getTime();
		if (now - node.time > cacheTime * 1000L) {
			remove(key);
			return null;
		}
		return node.object;
	}

	public int size() {
		return cache.size();
	}

	private synchronized void gc() {
		long now = new Date().getTime();
		List<String> removeKeys = new ArrayList<String>();
		for (Iterator<String> iterator = cache.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if (StringUtils.isEmpty(key)) {
				continue;
			}
			Node value = cache.get(key);
			if (value == null) {
				continue;
			}
			if (now - value.time > cacheTime * 1000L) {
				removeKeys.add(key);
			} else {
				break;
			}
		}
		for (String key : removeKeys) {
			cache.remove(key);
		}
		lastGcTime = now;
	}

	class Node {
		long time;
		Object object;

		Node(long time, Object object) {
			this.time = time;
			this.object = object;
		}
	}

}
