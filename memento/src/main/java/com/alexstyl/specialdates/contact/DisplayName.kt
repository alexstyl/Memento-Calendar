package com.alexstyl.specialdates.contact

data class DisplayName(val displayName: String, val allNames: Names, val firstNames: Names, val lastName: String) {

    fun hasMultipleFirstNames(): Boolean {
        return firstNames.count > 1
    }

    override fun toString(): String {
        return displayName
    }

    companion object {

        val NO_NAME = DisplayName("", Names.parse(""), Names.parse(""), "")
        private const val SEPARATOR = " "

        fun from(displayName: String?): DisplayName {
            if (displayName == null || displayName.isEmpty()) {
                return NO_NAME
            }
            val separatorIndex = indexOfLastSeparator(displayName)

            val firstNameString = subStringUpTo(displayName, separatorIndex).trim { it <= ' ' }

            val allNames = Names.parse(displayName)
            val firstNames = Names.parse(firstNameString)
            val lastNameString = subStringAfter(displayName, separatorIndex).trim { it <= ' ' }
            return DisplayName(displayName, allNames, firstNames, lastNameString)
        }

        private fun indexOfLastSeparator(displayName: String): Int {
            var lastSeparatorIndex = -1
            var currentIndex: Int
            do {
                currentIndex = displayName.indexOf(SEPARATOR, lastSeparatorIndex + 1)
                if (currentIndex != -1) {
                    lastSeparatorIndex = currentIndex
                }
            } while (currentIndex != -1)
            return lastSeparatorIndex
        }

        private fun subStringUpTo(displayName: String, stringLength: Int): String {
            if (displayName.length < stringLength) {
                return displayName
            }
            return if (stringLength == -1) {
                displayName
            } else displayName.substring(0, stringLength)
        }

        private fun subStringAfter(displayName: String, spaceIndex: Int): String {
            return if (spaceIndex == -1) {
                ""
            } else displayName.substring(spaceIndex, displayName.length).trim { it <= ' ' }

        }
    }
}
