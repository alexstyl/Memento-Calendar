package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.Namedays;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class GreeklishParserTest {

    private NamedayJSON namedayJSON;

    @Before
    public void setUp() throws JSONException {
        TestJSONResourceLoader resourceLoader = new TestJSONResourceLoader();
        NamedayJSONProvider resourceProvider = new NamedayJSONProvider(resourceLoader);
        namedayJSON = resourceProvider.getNamedayJSONFor(NamedayLocale.GREEK);

    }

    @Test
    public void alexandrosNamedayIsReturnedCorrectly() {
        Namedays namedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedays.getDatesFor("Αλέξανδρος");
        NameCelebrations datesGreeklish = namedays.getDatesFor("Aleksandros");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        Namedays namedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedays.getDatesFor("Δαβίδ");
        NameCelebrations datesGreeklish = namedays.getDatesFor("David");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void magdoulaNamedayIsReturnedCorrectly() {
        Namedays namedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = namedays.getDatesFor("Αμαλία");
        NameCelebrations datesGreeklish = namedays.getDatesFor("Amalia");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    private void assertThatContainsSamedate(NameCelebrations dates, NameCelebrations dates2) {
        assertThat(dates.getDates().get(0)).isEqualTo(dates2.getDates().get(0));
    }
}
