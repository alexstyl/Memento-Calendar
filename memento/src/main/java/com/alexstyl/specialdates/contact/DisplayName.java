package com.alexstyl.specialdates.contact;

import android.support.annotation.Nullable;

public class DisplayName {

    public static final DisplayName NO_NAME = new DisplayName("", Names.Companion.parse(""), Names.Companion.parse(""), "");
    private static final String SEPARATOR = " ";

    private final String displayName;
    private final Names allNames;
    private final Names firstNames;
    private final String lastName;

    public static DisplayName from(@Nullable String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            return NO_NAME;
        }
        int separatorIndex = indexOfLastSeparator(displayName);

        String firstNameString = subStringUpTo(displayName, separatorIndex).trim();

        Names allNames = Names.Companion.parse(displayName);
        Names firstNames = Names.Companion.parse(firstNameString);
        String lastNameString = subStringAfter(displayName, separatorIndex).trim();
        return new DisplayName(displayName, allNames, firstNames, lastNameString);
    }

    private DisplayName(String displayName, Names allNames, Names firstNames, String lastName) {
        this.displayName = displayName;
        this.allNames = allNames;
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

    boolean hasMultipleFirstNames() {
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

    public Names getAllNames() {
        return allNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DisplayName that = (DisplayName) o;

        if (!displayName.equals(that.displayName)) {
            return false;
        }
        if (!allNames.equals(that.allNames)) {
            return false;
        }
        if (!firstNames.equals(that.firstNames)) {
            return false;
        }
        return lastName.equals(that.lastName);

    }

    @Override
    public int hashCode() {
        int result = displayName.hashCode();
        result = 31 * result + allNames.hashCode();
        result = 31 * result + firstNames.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
