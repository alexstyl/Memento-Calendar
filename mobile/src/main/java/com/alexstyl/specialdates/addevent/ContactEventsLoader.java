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

final class ContactEventsLoader extends SimpleAsyncTaskLoader<List<AddEventContactEventViewModel>> {

    private final Optional<Contact> contact;

    private final PeopleEventsProvider peopleEventsProvider;
    private final AddEventContactEventViewModelFactory factory;
    private final AddEventViewModelFactory newEventFactory;
    private final List<AddEventContactEventViewModel> emptyEvents;

    ContactEventsLoader(Context context,
                        Optional<Contact> contact,
                        PeopleEventsProvider peopleEventsProvider,
                        AddEventContactEventViewModelFactory factory,
                        AddEventViewModelFactory newEventFactory) {
        super(context);
        this.contact = contact;
        this.peopleEventsProvider = peopleEventsProvider;
        this.factory = factory;
        this.newEventFactory = newEventFactory;
        this.emptyEvents = newEventFactory.createViewModelsForAllEventsBut(Collections.<EventType>emptyList());
    }

    @Override
    public List<AddEventContactEventViewModel> loadInBackground() {
        List<AddEventContactEventViewModel> existingViewModels;
        if (contact.isPresent()) {
            existingViewModels = createModelsFor(contact.get());
        } else {
            existingViewModels = emptyEvents;
        }

        Collections.sort(existingViewModels, EVENT_TYPE_ID_COMPARATOR);
        return existingViewModels;
    }

    private List<AddEventContactEventViewModel> createModelsFor(Contact contact) {
        List<AddEventContactEventViewModel> existingViewModels;
        List<ContactEvent> contactEvents = new ArrayList<>();
        List<ContactEvent> contactEventsOnDate = peopleEventsProvider.getCelebrationDateFor(TimePeriod.aYearFromNow());
        List<EventType> existingTypes = new ArrayList<>();
        for (ContactEvent contactEvent : contactEventsOnDate) {
            if (contactEvent.getContact().getContactID() == contact.getContactID() && isEditable(contactEvent)) {
                contactEvents.add(contactEvent);
                existingTypes.add(contactEvent.getType());
            }
        }
        existingViewModels = factory.createViewModel(contactEvents);
        List<AddEventContactEventViewModel> emptyViewModels = newEventFactory.createViewModelsForAllEventsBut(existingTypes);
        existingViewModels.addAll(emptyViewModels);
        return existingViewModels;
    }

    private boolean isEditable(ContactEvent contactEvent) {
        return contactEvent.getType() != StandardEventType.NAMEDAY
                && contactEvent.getType().getId() != StandardEventType.CUSTOM.getId();
    }

    private static final Comparator<AddEventContactEventViewModel> EVENT_TYPE_ID_COMPARATOR = new Comparator<AddEventContactEventViewModel>() {
        @Override
        public int compare(AddEventContactEventViewModel o1, AddEventContactEventViewModel o2) {
            return o1.getEventType().getId() - o2.getEventType().getId();
        }
    };

}
