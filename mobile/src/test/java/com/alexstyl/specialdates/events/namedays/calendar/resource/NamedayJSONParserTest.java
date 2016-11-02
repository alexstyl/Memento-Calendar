package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static com.alexstyl.specialdates.date.DateConstants.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class NamedayJSONParserTest {

    private NamedayJSON namedayJSON;

    @Before
    public void setUp() throws JSONException {
        JavaJSONResourceLoader resourceLoader = new JavaJSONResourceLoader();
        NamedayJSONResourceProvider resourceProvider = new NamedayJSONResourceProvider(resourceLoader);
        namedayJSON = resourceProvider.getNamedayJSONFor(NamedayLocale.gr);

    }

    @Test
    public void returningBundleHasNames() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        assertThat(namedayBundle.getNames()).isNotEmpty();
    }

    @Test
    public void alexandrosNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Αλέξανδρος");
        assertThatContainsDate(dates, Date.on(30, AUGUST));
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Δαβίδ");
        assertThatContainsDate(dates, Date.on(26, JUNE));
    }

    @Test
    public void magdoulaNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Αμαλία");
        assertThatContainsDate(dates, Date.on(10, JULY));
    }

    private void assertThatContainsDate(NameCelebrations celebrations, Date date) {
        boolean found = false;
        for (int i = 0; i < celebrations.getDates().size(); i++) {
            Date date1 = celebrations.getDate(i);
            if (date1.equals(date)) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Couldn't find date " + date + " inside " + celebrations);
        }
    }
}
