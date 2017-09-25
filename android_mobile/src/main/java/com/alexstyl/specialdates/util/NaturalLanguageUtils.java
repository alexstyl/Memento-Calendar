package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class NaturalLanguageUtils {

    private NaturalLanguageUtils() {
        // hide this
    }

    public static String joinContacts(Strings strings, Collection<Contact> iterable, int displayNo) {
        int size = iterable.size();
        if (size == 1) {
            return iterable.iterator().next().getDisplayName().toString();
        }

        ArrayList<String> names = new ArrayList<>(size);
        for (Contact contact : iterable) {
            names.add(contact.getGivenName());
        }
        return join(strings, names, displayNo);
    }

    public static String join(Strings strings, List<String> iterable, int displayNo) {
        if (iterable == null || iterable.size() == 0) {
            return "";
        }

        int size = iterable.size();
        if (size == 1) {
            return iterable.get(0);
        } else if (size == 2) {
            return strings.todayCelebrateTwo(iterable.get(0), iterable.get(1));
        }
        if (size < displayNo) {
            // set a cap of the number of items in the array
            displayNo = size;
        }

        StringBuilder str = new StringBuilder();
        int hasShown = 0;
        int toShow = displayNo - 1;
        if (toShow < 1) {
            toShow = 1;
        }
        for (int i = 0; i < toShow; i++) {
            if (str.length() != 0) {
                str.append(", ");
            }
            str.append(iterable.get(i));
            hasShown++;
        }

        int remaining = size - hasShown;
        if (remaining == 1) {
            // only one left
            return strings.todayCelebrateTwo(str.toString(), iterable.get(size - 1));
        } else {
            return strings.todayCelebrateMany(str.toString(), remaining);
        }

    }
}
