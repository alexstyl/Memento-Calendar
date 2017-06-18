package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.facebook.login.CredentialsExtractor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CredentialsExtractorTest {

    @Test

    public void givenPageSourceCredentialsAreExtractedProperly() {
        CredentialsExtractor extractor = new CredentialsExtractor();
        UserCredentials calendarURL = extractor.extractCalendarURL("<a href=\"webcal://www.facebook.com/ical/b.php?uid=1358181263&amp;key=AQCg1OoTtQTSzywU\">Birthdays</a>");
        assertThat(calendarURL.getUid()).isEqualTo(1358181263l);
        assertThat(calendarURL.getKey()).isEqualTo("AQCg1OoTtQTSzywU");

    }

    @Test

    public void givenAnyPageSourceCredentialsAreEmpty() {
        CredentialsExtractor extractor = new CredentialsExtractor();
        UserCredentials calendarURL = extractor.extractCalendarURL("");
        assertThat(calendarURL).isEqualTo(UserCredentials.ANNONYMOUS);

    }

}
