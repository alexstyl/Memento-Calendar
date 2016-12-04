package com.alexstyl.specialdates.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapList<K, V> {

    private final HashMap<K, List<V>> map = new HashMap<>();

    public boolean addValue(K key, V value) {
        List<V> listValue = map.get(key);
        if (listValue == null) {
            listValue = new ArrayList<>();
            map.put(key, listValue);
        }
        return listValue.add(value);
    }

    public List<V> get(K key) {
        return map.get(key);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
