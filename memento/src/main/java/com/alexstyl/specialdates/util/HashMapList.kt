package com.alexstyl.specialdates.util

class HashMapList<K, V> {

    private val map = HashMap<K, MutableList<V>>()

    fun addValue(key: K, value: V): Boolean {
        if (!map.containsKey(key)) {
            map.put(key, ArrayList())
        }
        return map[key]!!.add(value)
    }

    operator fun get(key: K): List<V>? = map[key]

    val isEmpty: Boolean
        get() = map.isEmpty()

    fun keys(): Set<K> = map.keys

    fun clear() {
        map.clear()
    }
}
