package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.alexstyl.specialdates.date.DateExtKt.dateOn;
import static com.alexstyl.specialdates.date.Months.APRIL;
import static com.alexstyl.specialdates.date.Months.FEBRUARY;
import static com.alexstyl.specialdates.date.Months.JANUARY;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ClosestEventsComparatorTest {

    private ClosestEventsComparator comparator = new ClosestEventsComparator();

    @Test
    public void bothOptionalsReturnZero() {
        assertThat(comparator.compare(absent(), absent())).isZero();
    }

    @Test
    public void onlyAIsPresentWins() {
        Optional<ContactEventsOnADate> optionalA = anOptional(dateOn(1, JANUARY, 1990));
        assertThat(comparator.compare(optionalA, absent())).isNegative();
    }

    @Test
    public void onlyBIsPresentWins() {
        Optional<ContactEventsOnADate> optionalB = anOptional(dateOn(1, JANUARY, 1990));
        assertThat(comparator.compare(absent(), optionalB)).isPositive();
    }

    @Test
    public void aWinsB() {
        Optional<ContactEventsOnADate> optionalA = anOptional(dateOn(1, JANUARY, 1990));
        Optional<ContactEventsOnADate> optionalB = anOptional(dateOn(5, FEBRUARY, 1990));
        assertThat(comparator.compare(optionalA, optionalB)).isNegative();
    }

    @Test
    public void bWinsA() {
        Optional<ContactEventsOnADate> optionalA = anOptional(dateOn(5, FEBRUARY, 1990));
        Optional<ContactEventsOnADate> optionalB = anOptional(dateOn(1, JANUARY, 1990));
        assertThat(comparator.compare(optionalA, optionalB)).isPositive();
    }

    @Test
    public void aPresentButBAbsent_thenAWins() {
        Optional<ContactEventsOnADate> optionalA = new Optional<>(ContactEventsOnADate.Companion.createFrom(dateOn(1, JANUARY, 1990), new ArrayList<ContactEvent>()));
        Optional<ContactEventsOnADate> optionalB = absent();

        assertThat(comparator.compare(optionalA, optionalB)).isNegative();
    }

    @Test
    public void sameDate_returnsZero() {
        Optional<ContactEventsOnADate> optionalA = anOptional(dateOn(5, APRIL, 2016));
        Optional<ContactEventsOnADate> optionalB = anOptional(dateOn(5, APRIL, 2016));

        assertThat(comparator.compare(optionalA, optionalB)).isZero();
    }

    private static Optional<ContactEventsOnADate> anOptional(Date date) {
        return new Optional<>(ContactEventsOnADate.Companion.createFrom(date, new ArrayList<ContactEvent>()));
    }

    private static Optional<ContactEventsOnADate> absent() {
        return Optional.Companion.absent();
    }
}
