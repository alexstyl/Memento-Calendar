package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.DisplayName
import java.text.Collator
import java.util.Locale

enum class NameMatcher {
    INSTANCE;

    private val collator: Collator = Collator.getInstance(Locale.getDefault())

    init {
        collator.strength = Collator.PRIMARY
    }

    fun match(displayName: DisplayName, searchQuery: String): Boolean {
        return oneOfTheNamesMatchesQuery(displayName, searchQuery) || searchQueryIsPartOfFullName(displayName, searchQuery)
    }

    private fun searchQueryIsPartOfFullName(displayName: DisplayName, searchQuery: String): Boolean {
        val fullDisplayName = displayName.toString()
        val partOfName = substring(fullDisplayName, searchQuery.length)

        return areEqual(partOfName, searchQuery)
    }

    private fun substring(string: String, length: Int): String {
        return if (string.length <= length) {
            string
        } else string.substring(0, length)
    }

    private fun oneOfTheNamesMatchesQuery(displayName: DisplayName, searchQuery: String): Boolean {
        val searchQueryLength = searchQuery.length
        val allNames = displayName.firstNames
        for (firstName in allNames) {
            val worthCheckingPart = substring(firstName, searchQueryLength)
            if (areEqual(searchQuery, worthCheckingPart)) {
                return true
            }
        }

        return checkIfLastNameMatches(displayName, searchQuery)
    }

    private fun checkIfLastNameMatches(displayName: DisplayName, searchQuery: String): Boolean {
        val lastName = displayName.lastName
        val searchQueryLength = searchQuery.length
        val worthCheckingPart = substring(lastName, searchQueryLength)
        return areEqual(worthCheckingPart, searchQuery)
    }

    private fun areEqual(displayName: String, worthCheckingPart: String): Boolean {
        return !(displayName.isEmpty() || worthCheckingPart.isEmpty()) && collator.compare(worthCheckingPart, displayName) == 0
    }

}
