package com.alexstyl.specialdates.upcoming;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class MonthLabelsTest {

    private MonthLabels monthLabels;

    private String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    @Before
    public void setup() {
        monthLabels = new MonthLabels(months);
    }

    @Test
    public void givenTheFirstMonthOfTheYear_thenTheCorrectLabelIsReturned() {
        int index = 1;
        String zeroIndexedLabel = monthLabels.getMonthOfYear(index);
        assertThat(zeroIndexedLabel).isEqualTo(months[index - 1]);
    }

    @Test
    public void givenTheLastMonthOfTheYear_thenTheCorrectLabelIsReturned() {
        int index = 12;
        String zeroIndexedLabel = monthLabels.getMonthOfYear(index);
        assertThat(zeroIndexedLabel).isEqualTo(months[index - 1]);
    }

    @Test
    public void copyOfMonthCreatesCorrectCoppy() {
        String[] copy = monthLabels.getMonthsOfYear();
        assertThat(copy).isEqualTo(months);
    }
}