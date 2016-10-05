package com.alexstyl.specialdates.date;

import android.content.res.Resources;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateConstants.JANUARY;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactEventTest {

    private static final Contact CONTACT_WITHOUT_BIRTHDAY = new TestContact(1, DisplayName.from("Peter"));
    private static final Date SOME_DATE = Date.on(1, 1, 1990);

    @Mock
    private Resources mockResources;

    @Before
    public void setUp() throws Exception {
        when(mockResources.getString(R.string.nameday)).thenReturn("Nameday");
        when(mockResources.getString(R.string.birthday)).thenReturn("Birthday");
        when(mockResources.getString(R.string.turns_age, 10)).thenReturn("Turns 10");

    }

    @Test
    public void labelForNameday() {
        ContactEvent contactEvent = new ContactEvent(EventType.NAMEDAY, SOME_DATE, CONTACT_WITHOUT_BIRTHDAY);
        String label = contactEvent.getLabel(mockResources);
        assertThat(label).isEqualTo("Nameday");
    }

    @Test
    public void labelForBirthdayWithoutYear() {
        ContactEvent contactEvent = new ContactEvent(EventType.BIRTHDAY, SOME_DATE, contactWithBirthdayOn(1, 1));
        String label = contactEvent.getLabel(mockResources);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearAfterDate() {
        Date eventDate = Date.on(1, 1, 1990);
        Contact contact = contactWithBirthdayOn(1, 1, 2016);
        ContactEvent contactEvent = new ContactEvent(EventType.BIRTHDAY, eventDate, contact);
        String label = contactEvent.getLabel(mockResources);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearOnDate() {
        Date eventDate = Date.on(1, JANUARY, 1990);
        Contact contact = contactWithBirthdayOn(1, JANUARY, 1990);
        ContactEvent contactEvent = new ContactEvent(EventType.BIRTHDAY, eventDate, contact);
        String label = contactEvent.getLabel(mockResources);
        assertThat(label).isEqualTo("Birthday");
    }

    @Test
    public void labelForBirthdayWithYearBeforeDate() {
        Date eventDate = Date.on(1, 1, 2000);
        Contact contact = contactWithBirthdayOn(1, 1, 1990);
        ContactEvent contactEvent = new ContactEvent(EventType.BIRTHDAY, eventDate, contact);
        String label = contactEvent.getLabel(mockResources);
        assertThat(label).isEqualTo("Turns 10");
    }

    private Contact contactWithBirthdayOn(int dayOfMonth, int month, int year) {
        return new TestContact(1, DisplayName.from("Peter"), Date.on(dayOfMonth, month, year));
    }

    private Contact contactWithBirthdayOn(int day, int month) {
        return new TestContact(1, DisplayName.from("Peter"), Date.on(day, month));
    }

}
