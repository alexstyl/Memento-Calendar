package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.WordComparator;

import java.util.ArrayList;
import java.util.List;

final class NameFilter {

    private static final int NAME_THREASHOLD = 5;

    private final WordComparator comparator;

    private final List<String> allNames;

    NameFilter(List<String> allNames, WordComparator comparator) {
        this.allNames = allNames;
        this.comparator = comparator;
    }

    List<String> performFiltering(CharSequence constraint) {
        List<String> names = new ArrayList<>();
        if (constraint.length() > 0) {
            int constraintLength = constraint.length();
            String typedName = constraint.toString();
            for (String existingName : allNames) {
                int nameLength = existingName.length();
                if (nameLength < constraintLength) {
                    continue;
                } else if (nameLength == constraintLength) {
                    if (comparator.compare(typedName, existingName)) {
                        // skip the existingName if the user has already typed it in
                        names.add(existingName);
                    }
                } else if (comparator.compareUpToPoint(existingName, typedName, constraintLength)) {
                    names.add(existingName);
                }
                if (names.size() == NAME_THREASHOLD) {
                    break;
                }
            }

        }
        return names;
    }

    List<String> getAllNames() {
        return allNames;
    }

}
