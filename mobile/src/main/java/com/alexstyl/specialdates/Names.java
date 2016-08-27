package com.alexstyl.specialdates;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Names implements Iterable<String> {

    private static final String SPACE_OR_SEMICOLUMN = " |-";

    private final List<String> names;

    public Names(List<String> names) {
        this.names = names;
    }

    public static Names from(String firstNameString) {
        String[] names = firstNameString.split(SPACE_OR_SEMICOLUMN);
        List<String> names1 = Arrays.asList(names);
        return new Names(names1);
    }

    public String getPrimary() {
        return names.get(0);
    }

    public int getCount() {
        return names.size();
    }

    @Override
    public Iterator<String> iterator() {
        return names.iterator();
    }
}
