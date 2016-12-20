package com.alexstyl.specialdates.addevent;

import android.content.Context;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class ContactEventsLoader extends SimpleAsyncTaskLoader<List<ContactEventViewModel>> {

    private final Optional<Contact> contact;

    private final PeopleEventsProvider peopleEventsProvider;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory newEventFactory;
    private final List<ContactEventViewModel> emptyEvents;

    ContactEventsLoader(Context context,
                        Optional<Contact> contact,
                        PeopleEventsProvider peopleEventsProvider,
                        ContactEventViewModelFactory factory,
                        AddEventViewModelFactory newEventFactory) {
        super(context);
        this.contact = contact;
        this.peopleEventsProvider = peopleEventsProvider;
        this.factory = factory;
        this.newEventFactory = newEventFactory;
        this.emptyEvents = newEventFactory.createViewModelsForAllEventsBut(Collections.<EventType>emptyList());
    }

    @Override
    public List<ContactEventViewModel> loadInBackground() {
        List<ContactEventViewModel> existingViewModels;
        if (contact.isPresent()) {
            existingViewModels = createModelsFor(contact.get());
        } else {
            existingViewModels = emptyEvents;
        }

        Collections.sort(existingViewModels, EVENT_TYPE_ID_COMPARATOR);
        return existingViewModels;
    }

    private List<ContactEventViewModel> createModelsFor(Contact contact) {
        List<ContactEventViewModel> existingViewModels;
        List<ContactEvent> contactEvents = new ArrayList<>();
        List<ContactEvent> contactEventsOnDate = peopleEventsProvider.getCelebrationDateFor(TimePeriod.aYearFromNow());
        List<EventType> existingTypes = new ArrayList<>();
        for (ContactEvent contactEvent : contactEventsOnDate) {
            if (contactEvent.getContact().getContactID() == contact.getContactID() && isEditable(contactEvent)) {
                contactEvents.add(contactEvent);
                existingTypes.add(contactEvent.getType());
            }
        }
        existingViewModels = factory.createViewModelsFor(contactEvents);
        List<ContactEventViewModel> emptyViewModels = newEventFactory.createViewModelsForAllEventsBut(existingTypes);
        existingViewModels.addAll(emptyViewModels);
        return existingViewModels;
    }

    private boolean isEditable(ContactEvent contactEvent) {
        return contactEvent.getType() != StandardEventType.NAMEDAY;
    }

    private static final Comparator<ContactEventViewModel> EVENT_TYPE_ID_COMPARATOR = new Comparator<ContactEventViewModel>() {
        @Override
        public int compare(ContactEventViewModel o1, ContactEventViewModel o2) {
            return o1.getEventType().getId() - o2.getEventType().getId();
        }
    };

}
