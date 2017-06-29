package com.alexstyl.specialdates.contact;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Names implements Iterable<String> {

    private static final String SPACE_OR_SEMICOLUMN = " |-";

    private final List<String> names;

    public static Names from(String names) {
        String[] allNames = names.split(SPACE_OR_SEMICOLUMN);
        return new Names(Arrays.asList(allNames));
    }

    private Names(List<String> names) {
        this.names = names;
    }

    public String getPrimary() {
        return names.get(0);
    }

    public int getCount() {
        return names.size();
    }

    public String get(int index) {
        return names.get(index);
    }

    @Override
    public Iterator<String> iterator() {
        return names.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Names strings = (Names) o;

        return names.equals(strings.names);

    }

    @Override
    public int hashCode() {
        return names.hashCode();
    }
}
