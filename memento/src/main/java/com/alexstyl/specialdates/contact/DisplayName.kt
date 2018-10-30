package com.alexstyl.specialdates.contact

data class DisplayName(private val displayName: String,
                       val allNames: List<String>,
                       val firstNames: List<String>,
                       val lastName: String) {

    fun hasMultipleFirstNames(): Boolean {
        return firstNames.size > 1
    }

    override fun toString(): String {
        return displayName
    }

    companion object {

        private val emptyName = DisplayName("", emptyList(), emptyList(), "")

        fun from(displayName: String?): DisplayName {
            // TODO ignore everything but letters & chars and move on...
            if (displayName == null || displayName.isEmpty()) {
                return emptyName
            }
            val allNames = displayName.split(delimiters = *arrayOf(" ", "-"), ignoreCase = true)
            val firstNames = if (allNames.size == 1) {
                allNames
            } else {
                allNames.subList(0, allNames.size - 1)
            }

            val lastName = if (allNames.size > 1) {
                allNames[allNames.size - 1]
            } else {
                ""
            }
            return DisplayName(displayName, allNames, firstNames, lastName)
        }
    }
}
