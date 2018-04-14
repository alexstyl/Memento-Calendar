package com.alexstyl.specialdates

/*
 * A simplified version of Java 8's Optional class.
 * See: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
data class Optional<T>(private val `object`: T?) {


    val isPresent: Boolean
        get() = `object` != null

    fun get(): T {
        if (!isPresent) {
            throw IllegalStateException("Optional was not present")
        }
        return `object`!!
    }

    operator fun contains(`object`: T?): Boolean {
        return if (isPresent) {
            get() == `object`
        } else {
            `object` == null
        }
    }

    override fun toString(): String {
        return `object`?.toString() ?: "absent"
    }

    companion object {
        fun <T> absent(): Optional<T> {
            return Optional(null)
        }
    }
}
