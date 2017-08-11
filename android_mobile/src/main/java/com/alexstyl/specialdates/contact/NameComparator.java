package com.alexstyl.specialdates.contact;

import com.alexstyl.gsc.SoundComparer;

class NameComparator {

    public static final NameComparator INSTANCE = new NameComparator();

    private NameComparator() {
        // hide this
    }

    public boolean areTheSameName(String o1, String o2) {
        return SoundComparer.areSame(o1, o2);
    }
}
