package com.hashimte.hashbus1.cache;

import android.util.LruCache;

import com.hashimte.hashbus1.model.Bus;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.Schedule;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    public static LruCache<String, Point> pointsCacheManager = new LruCache<>(4 * 1024 * 1024);
    public static LruCache<String, Journey> journeysCacheManager = new LruCache<>(4 * 1024 * 1024);
    public static LruCache<String, Schedule> scheduleCacheManager = new LruCache<>(4 * 1024 * 1024);

}
