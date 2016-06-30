package org.shisoft.hurricane.datastructure;

import java.util.Collection;
import java.util.Map;

/**
 * Created by shisoft on 30/6/2016.
 */
public interface SeqableMap<K, V> extends Map<K, V> {
    public Collection<K> keyColl();
}
