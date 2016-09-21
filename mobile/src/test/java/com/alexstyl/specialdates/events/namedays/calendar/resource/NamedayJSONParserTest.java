package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.AnnualEvent;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static com.alexstyl.specialdates.date.Date.*;
import static org.fest.assertions.api.Assertions.assertThat;

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
        assertThat(dates.getDate(0)).isEqualTo(new AnnualEvent(30, AUGUST));
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Δαβίδ");
        assertThat(dates.getDate(0)).isEqualTo(new AnnualEvent(26, JUNE));
    }

    @Test
    public void magdoulaNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Αμαλία");
        assertThat(dates.getDate(0)).isEqualTo(new AnnualEvent(10, JULY));
    }
}
