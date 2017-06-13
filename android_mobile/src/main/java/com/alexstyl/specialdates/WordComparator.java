package com.alexstyl.specialdates;

public interface WordComparator {

    boolean compare(String aWord, String anOtherWord);

    boolean compareUpToPoint(String aWord, String anOtherWord, int index);

}
