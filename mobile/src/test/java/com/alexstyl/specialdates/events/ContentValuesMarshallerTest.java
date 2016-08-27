package com.alexstyl.specialdates.events;

import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.DeviceContact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.PeopleEventsContract.PeopleEvents;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;


public class ContentValuesMarshallerTest {

//    private static final int BIRTHDAY = EventColumns.TYPE_BIRTHDAY;
//    private static final int ANY_YEAR = 2016;
//
//    private ContentValuesMarshaller marshaller;
//
//    @Before
//    public void setUp() {
//        marshaller = new ContentValuesMarshaller();
//    }
//
//    @Test
//    public void test() {
//        DeviceContact contact = givenContactWithBirthday();
//        ContactEvents eventList = givenListWithBirthdayOf(contact);
//        ContentValues marshalledValues = marshaller.marshall(eventList)[0];
//
//        assertThat(marshalledValues.get(PeopleEvents.CONTACT_ID)).isEqualTo(contact.getContactID());
//        assertThat(marshalledValues.get(PeopleEvents.DISPLAY_NAME)).isEqualTo(contact.getDisplayName());
//        assertThat(marshalledValues.get(PeopleEvents.SOURCE)).isEqualTo(DeviceContact.SOURCE_DEVICE);
//
//        assertThat(marshalledValues.get(PeopleEvents.EVENT_TYPE)).isEqualTo(EventColumns.TYPE_BIRTHDAY);
//        assertThat(marshalledValues.get(PeopleEvents.DATE)).isEqualTo(stringOf(contact.getBirthday()));
//
//
//    }
//
//    @NonNull
//    private DeviceContact givenContactWithBirthday() {
//        DeviceContact contact = DeviceContact.createDialogFor(5, "Alex Styl", anyString());
//        contact.setDateOfBirth(new Birthday(19, 12, 1990));
//        return contact;
//    }
//
//    private ContactEvents givenListWithBirthdayOf(DeviceContact contact) {
//        ContactEvents eventList = new ContactEvents();
//        Date dateOfBirth = contact.getBirthday();
//        DayDate birthday = DayDate.createDialogFor(dateOfBirth.getDayOfMonth(), dateOfBirth.getMonth(), ANY_YEAR);
//        eventList.add(ContactEvent.createDialogFor(BIRTHDAY, birthday, contact));
//        return eventList;
//    }
//
//    private static String stringOf(Date birthday) {
//        return DateDisplayStringCreator.getInstance().stringOf(birthday);
//    }

}
