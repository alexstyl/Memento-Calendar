package com.alexstyl.specialdates.upcoming

public inline fun <T> measure(tag: String, function: () -> T): T {
    val start = System.currentTimeMillis()
    val result = function()
    println("D/measure $tag took ${(System.currentTimeMillis() - start)} ms")
    return result
}

public inline fun <T> measure(tag: String, boolean: Boolean, function: () -> T): T =
        if (boolean) {
            measure(tag, function)
        } else {
            function()
        }

inline fun <T> measure(function: () -> T): T {
    return measure("", function)
}