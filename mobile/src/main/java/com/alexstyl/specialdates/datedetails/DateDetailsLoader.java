package com.alexstyl.specialdates.datedetails;

import android.content.Context;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class DateDetailsLoader extends SimpleAsyncTaskLoader<List<ContactEvent>> {

    private final Date date;
    private final ContactsObserver contactsObserver;

    private final Comparator<ContactEvent> displayNameComparator = new SpecialDateDisplayNameComparator();
    private final PeopleEventsProvider peopleEventsProvider;

    DateDetailsLoader(Context context, Date date, PeopleEventsProvider peopleEventsProvider, ContactsObserver contactsObserver) {
        super(context);
        this.date = date;
        this.contactsObserver = contactsObserver;
        this.peopleEventsProvider = peopleEventsProvider;

        contactsObserver.registerWith(
                new ContactsObserver.Callback() {
                    @Override
                    public void onContactsUpdated() {
                        onContentChanged();
                    }
                }
        );
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        contactsObserver.unregister();
    }

    @Override
    public List<ContactEvent> loadInBackground() {
        List<ContactEvent> celebrationDates = peopleEventsProvider.getCelebrationDateOn(date);
        Collections.sort(celebrationDates, displayNameComparator);
        return celebrationDates;
    }

    private class SpecialDateDisplayNameComparator implements Comparator<ContactEvent> {
        Collator col;

        {
            col = Collator.getInstance();
            col.setStrength(Collator.SECONDARY);
        }

        @Override
        public int compare(ContactEvent lhs, ContactEvent rhs) {
            return col.compare(lhs.getContact().getDisplayName().toString(), rhs.getContact().getDisplayName().toString());
        }

    }
}
