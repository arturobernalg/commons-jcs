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

package org.apache.jcs.yajcache.event;

import org.apache.jcs.yajcache.lang.annotation.*;
import org.apache.jcs.yajcache.core.ICache;
import org.apache.jcs.yajcache.core.ICacheChangeHandler;
/**
 *
 * @author Hanson Char
 */
@CopyRightApache
public abstract class CacheChangeEvent<V> extends java.util.EventObject {
    /** Creates a new instance of CacheEvent */
    protected CacheChangeEvent(@NonNullable ICache<V> cache) {
        super(cache);
    }
    /** Returns the cache which is the source of the events. */
    protected @NonNullable ICache<V> getCache() {
        return (ICache<V>)super.getSource();
    }
    /** 
     * Dispatches the event handling to the specific method invokation of the
     * given handler.
     */
    public abstract boolean dispatch(@NonNullable ICacheChangeHandler<V> handler);
}