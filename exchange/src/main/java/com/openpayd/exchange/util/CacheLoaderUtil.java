package com.openpayd.exchange.util;

import com.google.common.cache.CacheLoader;

import java.util.function.Function;

public class CacheLoaderUtil {
    private CacheLoaderUtil() {
    }

    public static <K, V> CacheLoader<K, V> create(Function<K, V> loader) {
        return new CacheLoader<K, V>() {
            @Override
            public V load(K k) {
                return loader.apply(k);
            }
        };
    }
}
