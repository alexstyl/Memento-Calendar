package com.alexstyl.specialdates;

import android.support.annotation.Nullable;

public class DisplayName {

    private final String displayName;
    private final Names firstNames;
    private final String lastName;

    private static final String SEPARATOR = " ";
    public static final DisplayName NO_NAME = new DisplayName("", Names.from(""), "");

    public static DisplayName from(@Nullable String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            return NO_NAME;
        }
        int separatorIndex = indexOfLastSeparator(displayName);

        String firstNameString = subStringUpTo(displayName, separatorIndex).trim();

        Names firstNames = Names.from(firstNameString);
        String lastNameString = subStringAfter(displayName, separatorIndex).trim();

        return new DisplayName(displayName, firstNames, lastNameString);
    }

    private DisplayName(String displayName, Names firstNames, String lastName) {
        this.displayName = displayName;
        this.firstNames = firstNames;
        this.lastName = lastName;
    }

    private static int indexOfLastSeparator(String displayName) {
        int lastSeparatorIndex = -1;
        int currentIndex;
        do {
            currentIndex = displayName.indexOf(SEPARATOR, lastSeparatorIndex + 1);
            if (currentIndex != -1) {
                lastSeparatorIndex = currentIndex;
            }
        } while (currentIndex != -1);
        return lastSeparatorIndex;
    }

    private static String subStringUpTo(String displayName, int stringLength) {
        if (displayName.length() < stringLength) {
            return displayName;
        }
        if (stringLength == -1) {
            return displayName;
        }
        return displayName.substring(0, stringLength);
    }

    private static String subStringAfter(String displayName, int spaceIndex) {
        if (spaceIndex == -1) {
            return "";
        }

        return displayName.substring(spaceIndex, displayName.length()).trim();
    }

    public boolean hasMultipleFirstNames() {
        return firstNames.getCount() > 1;
    }

    public String getLastName() {
        return lastName;
    }

    public Names getFirstNames() {
        return firstNames;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
