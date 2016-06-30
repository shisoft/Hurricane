package org.shisoft.hurricane.datastructure;

import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by shisoft on 30/6/2016.
 */
public class InMemoryMap<K, V> implements SeqableMap<K,V> {

    Map<K, V> intialMap;

    public InMemoryMap(Map<K, V> intialMap) {
        this.intialMap = intialMap;
    }

    public InMemoryMap() {
        this(HashObjObjMaps.newMutableMap());
    }

    @Override
    public Collection<K> keyColl() {
        return intialMap.keySet();
    }

    @Override
    public int size() {
        return intialMap.size();
    }

    @Override
    public boolean isEmpty() {
        return intialMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return intialMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return intialMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return intialMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return intialMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return intialMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        intialMap.putAll(m);
    }

    @Override
    public void clear() {
        intialMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return intialMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return intialMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return intialMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this.intialMap.equals(o);
    }

    @Override
    public int hashCode() {
        return intialMap.hashCode();
    }
}
