
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jcs.yajcache.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.jcs.yajcache.soft.SoftRefCacheSafe;
import org.apache.jcs.yajcache.annotate.*;

/**
 * @author Hanson Char
 */
@CopyRightApache
public enum SafeCacheManager {
    inst;
    // Cache name to Cache mapping.
    private final @NonNullable ConcurrentMap<String,ICacheSafe> map = 
            new ConcurrentHashMap<String, ICacheSafe>();
    /** 
     * Returns the cache for the specified name and value type;  
     * Creates the cache if necessary.
     *
     * @throws ClassCastException if the cache already exists for an
     * incompatible value type.
     */
    public @NonNullable <V> ICacheSafe<V> getCache(@NonNullable String name, @NonNullable Class<V> valueType)
    {
        ICacheSafe c = this.map.get(name);
               
        if (c == null)
            c = this.createCache(name, valueType);
        else
            CacheManagerUtils.inst.checkValueType(c, valueType);
        return c;
    }
    /** 
     * Returns an existing cache for the specified name; or null if not found.
     */
    public ICacheSafe getCache(@NonNullable String name) {
        return this.map.get(name);
    }
    /**
     * Removes the specified cache, if it exists.
     */
    public ICacheSafe removeCache(@NonNullable String name) {
        ICacheSafe c = this.map.remove(name);
        
        if (c != null) {
            c.clear();
        }
        return c;
    }
    /** 
     * Creates the specified cache if not already created.
     * 
     * @return either the cache created by the current thread, or
     * an existing cache created earlier by another thread.
     */
    private @NonNullable <V> ICacheSafe<V> createCache(
            @NonNullable String name, @NonNullable Class<V> valueType) 
    {
        ICacheSafe c = new SoftRefCacheSafe<V>(name, valueType);
        ICacheSafe old = this.map.putIfAbsent(name, c);

        if (old != null) {
            // race condition: cache already created by another thread.
            CacheManagerUtils.inst.checkValueType(old, valueType);
            c = old;
        }
        return c;
    }

    @TestOnly("Used soley to simluate a race condition during cache creation")
    @NonNullable <V> ICacheSafe<V> testCreateCacheRaceCondition(
            @NonNullable String name, @NonNullable Class<V> valueType)
    {
        return this.createCache(name, valueType);
    }
}
