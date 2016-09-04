package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.WordComparator;

import java.text.Collator;

class CaseInsensitiveComparator implements WordComparator {

    private final Collator collator = Collator.getInstance();

    public CaseInsensitiveComparator() {
        this.collator.setStrength(Collator.PRIMARY);
    }

    @Override
    public boolean compare(String aWord, String anOtherWord) {
        return collator.compare(aWord, anOtherWord) == 0;
    }

    @Override
    public boolean compareUpToPoint(String aWord, String anOtherWord, int index) {
        int myLength = aWord.length();
        String theWord;
        if (myLength > index) {
            theWord = aWord.substring(0, index);
        } else {
            theWord = aWord;
        }

        int otherLength = anOtherWord.length();
        String theOtherWord;
        if (otherLength > index) {
            theOtherWord = anOtherWord.substring(0, index);
        } else {
            theOtherWord = anOtherWord;
        }
        return collator.compare(theWord, theOtherWord) == 0;

    }

}


