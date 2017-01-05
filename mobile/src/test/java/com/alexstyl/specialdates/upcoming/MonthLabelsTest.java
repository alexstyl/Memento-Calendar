package com.alexstyl.specialdates.upcoming;

import org.junit.Before;
import org.junit.Test;

import static com.alexstyl.specialdates.date.DateConstants.DECEMBER;
import static com.alexstyl.specialdates.date.DateConstants.JANUARY;
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
        int index = JANUARY;
        String zeroIndexedLabel = monthLabels.getMonthOfYear(index);
        assertThat(zeroIndexedLabel).isEqualTo("January");
    }

    @Test
    public void givenTheLastMonthOfTheYear_thenTheCorrectLabelIsReturned() {
        int index = DECEMBER;
        String zeroIndexedLabel = monthLabels.getMonthOfYear(index);
        assertThat(zeroIndexedLabel).isEqualTo("December");
    }

    @Test
    public void copyOfMonthCreatesCorrectCoppy() {
        String[] copy = monthLabels.getMonthsOfYear();
        assertThat(copy).isEqualTo(months);
    }
}
