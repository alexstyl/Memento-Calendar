package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.RomanianNamedays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class RomanianNamedaysTest {

    @Test
    public void whenCreatingRomanianNamedaysAllNamesAreReturnedCorrectly() throws Exception {
        List<String> celebratingNames = Arrays.asList("Florin", "Viorel", "Viorica", "Florin");

        RomanianNamedays namedays = RomanianNamedays.from(celebratingNames);

        ArrayList<String> allNames = namedays.getAllNames();
        assertThat(allNames).isEqualTo(celebratingNames);
    }

    @Test
    public void whenCreatingRomanianNamedaysDateIsCalculatedProperly() throws Exception {
        List<String> expectedNames = Arrays.asList("Florin", "Viorel", "Viorica", "Florin");

        RomanianNamedays namedays = RomanianNamedays.from(expectedNames);

        List<DayDate> expectedDates = buildExpectedDates();
        for (DayDate expectedDate : expectedDates) {
            NamesInADate allNames = namedays.getNamedaysFor(expectedDate);
            List<String> actualNames = allNames.getNames();
            if (!expectedNames.equals(actualNames)) {
                fail(String.format(Locale.US, "Year [%d] did not contain the correct names [%s]", expectedDate.getYear(), actualNames));
            }
        }
    }

    private List<DayDate> buildExpectedDates() {
        List<DayDate> dayDates = new ArrayList<>();
        dayDates.add(DayDate.newInstance(9, 4, 2017));
        dayDates.add(DayDate.newInstance(1, 4, 2018));
        dayDates.add(DayDate.newInstance(21, 4, 2019));
        dayDates.add(DayDate.newInstance(12, 4, 2020));
        dayDates.add(DayDate.newInstance(25, 4, 2021));
        dayDates.add(DayDate.newInstance(17, 4, 2022));
        return dayDates;
    }
}
