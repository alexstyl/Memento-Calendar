package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.StaticNamedays;
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
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Αλέξανδρος");
        NameCelebrations datesGreeklish = staticNamedays.getDatesFor("Aleksandros");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void davidNamedayIsReturnedCorrectly() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Δαβίδ");
        NameCelebrations datesGreeklish = staticNamedays.getDatesFor("David");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    @Test
    public void magdoulaNamedayIsReturnedCorrectly() {
        StaticNamedays staticNamedays = NamedayJSONParser.INSTANCE.getNamedaysFromJSONasSounds(namedayJSON);
        NameCelebrations dates = staticNamedays.getDatesFor("Αμαλία");
        NameCelebrations datesGreeklish = staticNamedays.getDatesFor("Amalia");
        assertThatContainsSamedate(dates, datesGreeklish);
    }

    private void assertThatContainsSamedate(NameCelebrations dates, NameCelebrations dates2) {
        assertThat(dates.getDates().get(0)).isEqualTo(dates2.getDates().get(0));
    }
}
