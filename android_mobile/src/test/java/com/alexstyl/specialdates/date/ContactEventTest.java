package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.JavaStrings;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactFixture;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.Months.JANUARY;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ContactEventTest {

    private static final Optional<Long> NO_DEVICE_ID = Optional.absent();
    private static final Date SOME_DATE = Date.Companion.on(1, JANUARY, 1990);
    private static final Date SOME_DATE_WITHOUT_YEAR = Date.Companion.on(1, JANUARY);
    private static final int CURRENT_YEAR = Date.Companion.getCURRENT_YEAR();

    private Contact ANY_CONTACT = ContactFixture.aContact();

    private Strings strings = new JavaStrings();

    @Test
    public void labelForNameday() {
        ContactEvent contactEvent = new ContactEvent(NO_DEVICE_ID, StandardEventType.NAMEDAY, SOME_DATE, ANY_CONTACT);
        String label = contactEvent.getLabel(SOME_DATE, strings);
        assertThat(label).isEqualTo("Nameday");
    }

    @Test
    public void labelForBirthdayWithoutYear() {
        ContactEvent contactEvent = new ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, SOME_DATE_WITHOUT_YEAR, ANY_CONTACT);
        String label = contactEvent.getLabel(SOME_DATE, strings);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearAfterDate() {
        Date eventDate = Date.Companion.on(1, JANUARY, CURRENT_YEAR + 50);
        ContactEvent contactEvent = new ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT);
        String label = contactEvent.getLabel(SOME_DATE, strings);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearOnDate() {
        Date eventDate = Date.Companion.on(1, JANUARY, CURRENT_YEAR);
        ContactEvent contactEvent = new ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT);
        String label = contactEvent.getLabel(SOME_DATE, strings);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearBeforeDate() {
        Date eventDate = Date.Companion.on(1, JANUARY, CURRENT_YEAR - 10);

        ContactEvent contactEvent = new ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT);
        String label = contactEvent.getLabel(Date.Companion.on(1, JANUARY, Date.Companion.today().getYear()), strings);
        assertThat(label).isEqualTo("Turns 10");
    }

}
