package com.alexstyl.specialdates.contact


data class Names(private val names: List<String>) : Iterable<String> {

    val primary: String
        get() = names[0]

    val count: Int
        get() = names.size

    override fun iterator(): Iterator<String> = names.iterator()

    operator fun get(index: Int): String = names[index]

    companion object {

        private val REGEX = "[@a-zA-Zά-ώΆ-Ώα-ωΑ-Ω\\d]+".toRegex()

        private val cache = mutableMapOf<String, Names>()

        fun parse(input: String): Names {
            if (cache.containsKey(input)) {
                return cache[input]!!
            } else {
                val newNames = Names(REGEX.findAll(input)
                        .map { it.value }
                        .toList())
                cache.put(input, newNames)
                return newNames
            }
        }

    }
}
