package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.RomanianEasterSpecialCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.RomanianNamedays;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.alexstyl.specialdates.date.DateExtKt.dateOn;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class RomanianNamedaysTest {

    private final RomanianEasterSpecialCalculator calculator = new RomanianEasterSpecialCalculator(new OrthodoxEasterCalculator());

    @Test
    public void whenCreatingRomanianNamedaysAllNamesAreReturnedCorrectly() throws Exception {
        List<String> celebratingNames = Arrays.asList("Florin", "Viorel", "Viorica", "Florin");

        RomanianNamedays namedays = new RomanianNamedays(calculator, celebratingNames);

        ArrayList<String> allNames = namedays.getAllNames();
        assertThat(allNames).isEqualTo(celebratingNames);
    }

    @Test
    public void whenCreatingRomanianNamedaysDateIsCalculatedProperly() throws Exception {
        List<String> expectedNames = Arrays.asList("Florin", "Viorel", "Viorica", "Florin");

        RomanianNamedays namedays = new RomanianNamedays(calculator, expectedNames);

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
        dates.add(dateOn(9, 4, 2017));
        dates.add(dateOn(1, 4, 2018));
        dates.add(dateOn(21, 4, 2019));
        dates.add(dateOn(12, 4, 2020));
        dates.add(dateOn(25, 4, 2021));
        dates.add(dateOn(17, 4, 2022));
        return dates;
    }
}
