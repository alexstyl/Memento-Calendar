package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.resource.RomanianNamedays;

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

        List<String> allNames = namedays.getAllNames();
        assertThat(allNames).isEqualTo(celebratingNames);
    }

    @Test
    public void whenCreatingRomanianNamedaysDateIsCalculatedProperly() throws Exception {
        List<String> expectedNames = Arrays.asList("Florin", "Viorel", "Viorica", "Florin");

        RomanianNamedays namedays = RomanianNamedays.from(expectedNames);

        List<Date> expectedDates = buildExpectedDates();
        for (Date expectedDate : expectedDates) {
            NamesInADate allNames = namedays.getNamedaysFor(expectedDate);
            List<String> actualNames = allNames.getNames();
            if (!expectedNames.equals(actualNames)) {
                fail(String.format(Locale.US, "Year [%d] did not contain the correct names [%s]", expectedDate.getYear(), actualNames));
            }
        }
    }

    private List<Date> buildExpectedDates() {
        List<Date> dates = new ArrayList<>();
        dates.add(Date.on(9, 4, 2017));
        dates.add(Date.on(1, 4, 2018));
        dates.add(Date.on(21, 4, 2019));
        dates.add(Date.on(12, 4, 2020));
        dates.add(Date.on(25, 4, 2021));
        dates.add(Date.on(17, 4, 2022));
        return dates;
    }
}
