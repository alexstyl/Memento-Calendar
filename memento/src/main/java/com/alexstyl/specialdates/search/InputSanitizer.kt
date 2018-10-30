package com.alexstyl.specialdates.search

import java.lang.StringBuilder

object InputSanitizer {

    fun removeNonAlphaNumericFrom(input: String): String {
        val chars = input.toCharArray()
        var first = 0

        val str = StringBuilder()
        while (first <= chars.size) {

            while (first < chars.size && !chars[first].isLetterOrDigit()) {
                first++
            }
            if (first > chars.size) {
                break
            }
            // found a start of a word
            var second = first

            while (second < chars.size && chars[second].isLetterOrDigit()) {
                second++
            }

            if (second > chars.size) {
                break
            }
            if (str.isNotEmpty() && second != first) {
                str.append(" ")
            }
            str.append(input.substring(first, second))
            first = second + 1
        }

        return str.toString()
    }
}