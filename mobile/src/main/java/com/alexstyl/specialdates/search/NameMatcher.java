package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Names;

import java.text.Collator;
import java.util.Locale;

class NameMatcher {

    private final Collator collator;

    static NameMatcher newInstance() {
        Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.PRIMARY);
        return new NameMatcher(collator);
    }

    private NameMatcher(Collator collator) {
        this.collator = collator;
    }

    public boolean match(DisplayName displayName, String searchQuery) {
        if (oneOfTheNamesMatchesQuery(displayName, searchQuery)) {
            return true;
        }
        return searchQueryIsPartOfFullName(displayName, searchQuery);
    }

    private boolean searchQueryIsPartOfFullName(DisplayName displayName, String searchQuery) {
        String fullDisplayName = displayName.toString();
        String partOfName = substring(fullDisplayName, searchQuery.length());

        return areEqual(partOfName, searchQuery);
    }

    private String substring(String string, int length) {
        if (string.length() <= length) {
            return string;
        }
        return string.substring(0, length);
    }

    private boolean oneOfTheNamesMatchesQuery(DisplayName displayName, String searchQuery) {
        int searchQueryLength = searchQuery.length();
        Names allNames = displayName.getFirstNames();
        for (String firstName : allNames) {
            String worthCheckingPart = substring(firstName, searchQueryLength);
            if (areEqual(searchQuery, worthCheckingPart)) {
                return true;
            }
        }

        return checkIfLastNameMatches(displayName, searchQuery);
    }

    private boolean checkIfLastNameMatches(DisplayName displayName, String searchQuery) {
        String lastName = displayName.getLastName();
        int searchQueryLength = searchQuery.length();
        String worthCheckingPart = substring(lastName, searchQueryLength);
        return areEqual(worthCheckingPart, searchQuery);
    }

    private boolean areEqual(String displayName, String worthCheckingPart) {
        if (isEmpty(displayName) || isEmpty(worthCheckingPart)) {
            return false;
        }
        return collator.compare(worthCheckingPart, displayName) == 0;
    }

    private boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
