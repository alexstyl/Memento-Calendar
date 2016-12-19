package com.alexstyl.specialdates.addevent;

import android.content.Context;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.ArrayList;
import java.util.List;

final class ContactEventsLoader extends SimpleAsyncTaskLoader<List<ContactEventViewModel>> {

    private final Contact contact;
    private final PeopleEventsProvider peopleEventsProvider;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory newEventFactory;

    ContactEventsLoader(Context context,
                        Contact contact,
                        PeopleEventsProvider peopleEventsProvider,
                        ContactEventViewModelFactory factory,
                        AddEventViewModelFactory newEventFactory) {
        super(context);
        this.contact = contact;
        this.peopleEventsProvider = peopleEventsProvider;
        this.factory = factory;
        this.newEventFactory = newEventFactory;
    }

    @Override
    public List<ContactEventViewModel> loadInBackground() {
        List<ContactEvent> contactEventsOnDate = peopleEventsProvider.getCelebrationDateFor(TimePeriod.aYearFromNow());

        List<ContactEvent> contactEvents = new ArrayList<>();
        List<EventType> existingTypes = new ArrayList<>();
        for (ContactEvent contactEvent : contactEventsOnDate) {
            if (contactEvent.getContact().getContactID() == contact.getContactID() && isEditable(contactEvent)) {
                contactEvents.add(contactEvent);
                existingTypes.add(contactEvent.getType());
            }
        }
        List<ContactEventViewModel> existingViewModels = factory.createViewModelsFor(contactEvents);
        List<ContactEventViewModel> emptyViewModels = newEventFactory.createViewModelsForAllEventsBut(existingTypes);

        existingViewModels.addAll(emptyViewModels);
        return existingViewModels;
    }

    private boolean isEditable(ContactEvent contactEvent) {
        return contactEvent.getType() != StandardEventType.NAMEDAY;
    }

}
