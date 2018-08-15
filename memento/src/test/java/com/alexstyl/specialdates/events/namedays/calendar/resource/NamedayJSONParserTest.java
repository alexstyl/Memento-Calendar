package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.StaticNamedays;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.alexstyl.specialdates.date.Months.AUGUST;
import static com.alexstyl.specialdates.date.Months.JULY;
import static com.alexstyl.specialdates.date.Months.JUNE;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class NamedayJSONParserTest {

    private NamedayJSON namedayJSON;

    @Before
    public void setUp() throws JSONException {
        TestJSONResourceLoader resourceLoader = new TestJSONResourceLoader();
        NamedayJSONProvider resourceProvider = new NamedayJSONProvider(resourceLoader);
        namedayJSON = resourceProvider.getNamedayJSONFor(NamedayLocale.GREEK);

    }

    @Test
    public void returningBundleHasNames() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFrom(namedayJSON);
        assertThat(staticNamedays.getNames()).isNotEmpty();
    }

    @Test
    public void alexandrosNamedayIsReturnedCorrectly() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Αλέξανδρος");
        assertThatContainsDate(dates, Date.Companion.on(30, AUGUST));
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Δαβίδ");
        assertThatContainsDate(dates, Date.Companion.on(26, JUNE));
    }

    @Test
    public void amaliaNamedayIsReturnedCorrectly() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Αμαλία");
        assertThatContainsDate(dates, Date.Companion.on(10, JULY));
    }

    private static void assertThatContainsDate(NameCelebrations celebrations, Date date) {
        List<Date> dates = celebrations.getDates();
        int index = 0;
        while (index < dates.size()) {
            if (celebrations.getDates().get(index).equals(date)) {
                return;
            }
            index++;
        }

        fail("Couldn't find date " + date + " inside " + celebrations);
    }
}
