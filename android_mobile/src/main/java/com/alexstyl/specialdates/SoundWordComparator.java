package com.alexstyl.specialdates;

import com.alexstyl.gsc.SoundComparer;

public class SoundWordComparator implements WordComparator {

    @Override
    public boolean compare(String aWord, String anOtherWord) {
        return SoundComparer.Companion.soundTheSame(aWord, anOtherWord);
    }

    @Override
    public boolean compareUpToPoint(String aWord, String anOtherWord, int index) {
        return SoundComparer.Companion.startsWith(aWord, anOtherWord);
    }
}
