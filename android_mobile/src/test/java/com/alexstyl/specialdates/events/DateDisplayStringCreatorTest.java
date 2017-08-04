package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;

import org.junit.BeforeClass;
import org.junit.Test;

import static com.alexstyl.specialdates.date.DateConstants.JANUARY;
import static com.alexstyl.specialdates.date.DateConstants.MAY;
import static org.fest.assertions.api.Assertions.assertThat;

public class DateDisplayStringCreatorTest {

    private static DateDisplayStringCreator creator;

    @BeforeClass
    public static void init() {
        creator = DateDisplayStringCreator.INSTANCE;
    }

    @Test
    public void givenDateWithYear_thenReturningStringIsCorrect() {
        Date date = Date.Companion.on(5, MAY, 1995);
        String dateToString = creator.stringOf(date);
        assertThat(dateToString).isEqualTo("1995-05-05");
    }

    @Test
    public void givenDateWithNoYear_thenReturningStringIsCorrect() {
        Date date = Date.Companion.on(5, MAY);
        String dateToString = creator.stringOf(date);
        assertThat(dateToString).isEqualTo("--05-05");
    }

    @Test
    public void toShortDateWithYear() {
        Date date = Date.Companion.on(1, JANUARY, 1990);
        assertThat(creator.stringOf(date)).isEqualTo("1990-01-01");
    }

}
