package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class GreeklishParserTest {

    private NamedayJSON namedayJSON;

    @Before
    public void setUp() throws JSONException {
        JavaJSONResourceLoader resourceLoader = new JavaJSONResourceLoader();
        NamedayJSONResourceProvider resourceProvider = new NamedayJSONResourceProvider(resourceLoader);
        namedayJSON = resourceProvider.getNamedayJSONFor(NamedayLocale.gr);

    }

    @Test
    public void alexandrosNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Αλέξανδρος");
        NameCelebrations datesGreeklish = namedayBundle.getDatesFor("Aleksandros");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Δαβίδ");
        NameCelebrations datesGreeklish = namedayBundle.getDatesFor("David");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void magdoulaNamedayIsReturnedCorrectly() {
        NamedayBundle namedayBundle = NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedayBundle.getDatesFor("Αμαλία");
        NameCelebrations datesGreeklish = namedayBundle.getDatesFor("Amalia");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    private void assertThatContainsSamedate(NameCelebrations dates, NameCelebrations dates2) {
        assertThat(dates.getDate(0)).isEqualTo(dates2.getDate(0));
    }
}
