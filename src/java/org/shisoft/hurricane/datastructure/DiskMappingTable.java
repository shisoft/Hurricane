package org.shisoft.hurricane.datastructure;

import clojure.lang.IFn;
import net.openhft.koloboke.collect.map.hash.HashObjLongMap;
import net.openhft.koloboke.collect.map.hash.HashObjLongMaps;
import org.shisoft.hurricane.BufferedRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by shisoft on 29/6/2016.
 */
public class DiskMappingTable<K, V> implements SeqableMap<K, V> {

    private final RandomAccessFile raf;
    private final HashObjLongMap<K> diskMap;
    private final AtomicLong current;
    private final IFn encoder;
    private final IFn decoder;
    private boolean seqRead;
    private List<K> seqList;
    private String filePath;


    public DiskMappingTable(String filePath, IFn encoder, IFn decoder, boolean seqRead) throws IOException {
        raf = new BufferedRandomAccessFile(filePath, "rw", 8388608);
        current = new AtomicLong(0);
        diskMap = HashObjLongMaps.newMutableMap();
        this.encoder = encoder;
        this.decoder = decoder;
        this.seqRead = seqRead;
        this.filePath = filePath;
        if (seqRead) {
            this.seqList = new ArrayList<K>();
        }
    }

    public DiskMappingTable(String filePath, IFn encoder, IFn decoder) throws IOException {
        this(filePath, encoder, decoder, false);
    }

    @Override
    public int size() {
        synchronized (diskMap) {
            return diskMap.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (diskMap) {
            return diskMap.isEmpty();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (diskMap) {
            return diskMap.containsKey(key);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        synchronized (diskMap) {
            long loc = diskMap.getOrDefault(key, -1);
            if (loc < 0) {
                return null;
            } else {
                synchronized (raf) {
                    try {
                        raf.seek(loc);
                        int dataLen = raf.readInt();
                        byte[] bs = new byte[dataLen];
                        raf.read(bs);
                        return (V) decoder.invoke(bs);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
    }

    @Override
    public V put(K key, V value) {
        synchronized (diskMap) {
            byte[] bs = (byte[]) encoder.invoke(value);
            int dataLen = bs.length;
            long totalLen = dataLen + Integer.BYTES;
            long loc = current.getAndAdd(totalLen);
            try {
                raf.seek(loc);
                raf.writeInt(dataLen);
                raf.write(bs);
                if (seqRead) {
                    if (diskMap.containsKey(key)){
                        seqList.remove(key);
                    }
                    seqList.add(key);
                }
                diskMap.put(key, loc);
                return value;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public V remove(Object key) {
        synchronized (diskMap) {
            if (seqRead) seqList.remove(key);
            return (V) diskMap.remove(key);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        synchronized (diskMap) {
            if (seqRead) seqList.clear();
            diskMap.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        synchronized (diskMap) {
            return diskMap.keySet();
        }
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<K> keyColl() {
        if (seqRead) {
            return seqList;
        } else {
            return diskMap.keySet();
        }
    }

    @Override
    public void dispose() {
        diskMap.clear();
        new File(filePath).delete();
    }
}
