package com.alexstyl.specialdates.contact


data class Names constructor(private val names: List<String>) : Iterable<String> {

    val primary: String
        get() = names[0]

    val count: Int
        get() = names.size

    override fun iterator(): Iterator<String> = names.iterator()

    operator fun get(index: Int): String = names[index]

    companion object {

        private val regex = "[a-zA-Zά-ώΆ-Ώα-ωΑ-Ω\\d]+"

        fun from(input: String): Names =
                Names(findAllNamesIn(input)
                        .toList()
                        .map { it.value }
                )

        private fun findAllNamesIn(input: String) = regex.toRegex().findAll(input)
    }
}
