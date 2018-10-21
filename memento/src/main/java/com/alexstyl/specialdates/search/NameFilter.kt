package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.NameComparator

import java.util.ArrayList

class NameFilter(val allNames: List<String>, private val comparator: NameComparator) {

    fun performFiltering(constraint: CharSequence): List<String> {
        val names = ArrayList<String>()
        if (constraint.isNotEmpty()) {
            val constraintLength = constraint.length
            val typedName = constraint.toString()
            for (existingName in allNames) {
                val nameLength = existingName.length
                if (nameLength < constraintLength) {
                    continue
                } else if (nameLength == constraintLength) {
                    if (comparator.compare(typedName, existingName)) {
                        // skip the existingName if the user has already typed it in
                        names.add(existingName)
                    }
                } else if (comparator.startsWith(existingName, typedName)) {
                    names.add(existingName)
                }
                if (names.size == NAME_THREASHOLD) {
                    break
                }
            }

        }
        return names
    }

    companion object {
        const val NAME_THREASHOLD = 5
    }

}
