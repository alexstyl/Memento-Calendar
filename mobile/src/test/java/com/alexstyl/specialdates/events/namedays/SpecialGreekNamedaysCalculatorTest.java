package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecialGreekNamedaysCalculatorTest {

    private static final DayDate easter = easterForTheYear(2016);
    private static final DayDate EXPECTED_DATE = DayDate.newInstance(19, DayDate.MARCH, 2016);

    private static final int DAYS_TO_EASTER = -43;

    private static final EasternNameday UPCOMING_NAMEDAY = new EasternNameday(
            DAYS_TO_EASTER, Arrays.asList("Δώρα")
    );

//    @Test
//    public void givenAEasterNamedayFortyThreeDaysBeforeEaster_whenYearIs2016_NamedayDateIsTheOneExpected() {
//
//        List<EasternNameday> easternNamedays = givenNameday43DaysBeforeEaster();
//        SpecialGreekNamedaysCalculator calculator = new SpecialGreekNamedaysCalculator(easternNamedays);
//        NamedayBundle marshalledList = calculator.calculateForEasterDate(easter);
//        NamesInADate namedaysFor = marshalledList.getNamedaysFor(EXPECTED_DATE);
//        assertThat(namedaysFor).isNotNull();
//
//    }

    private List<EasternNameday> givenNameday43DaysBeforeEaster() {
        ArrayList<EasternNameday> namedays = new ArrayList<>(1);
        namedays.add(UPCOMING_NAMEDAY);
        return namedays;
    }

    private static DayDate easterForTheYear(int year) {
        return new EasterCalculator().calculateEasterForYear(year);
    }
}
