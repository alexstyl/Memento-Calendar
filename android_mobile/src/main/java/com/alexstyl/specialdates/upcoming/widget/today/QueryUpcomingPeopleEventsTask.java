package com.alexstyl.specialdates.upcoming.widget.today;

import android.os.AsyncTask;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.NoEventsFoundException;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;

abstract class QueryUpcomingPeopleEventsTask extends AsyncTask<Object, Object, Optional<ContactEventsOnADate>> {

    private final PeopleEventsProvider eventsProvider;

    QueryUpcomingPeopleEventsTask(PeopleEventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @Override
    protected Optional<ContactEventsOnADate> doInBackground(Object... params) {
        Date today = Date.Companion.today();
        try {
            Optional<Date> optional = new Optional<>(eventsProvider.findClosestEventDateOnOrAfter(today));
            if (optional.isPresent()) {
                ContactEventsOnADate events = eventsProvider.fetchEventsOn(optional.get());
                return new Optional<>(events);
            }
        } catch (NoEventsFoundException e) {
            return Optional.absent();
        }
        return Optional.absent();
    }

    @Override
    protected void onPostExecute(Optional<ContactEventsOnADate> celebrationDate) {
        super.onPostExecute(celebrationDate);
        if (celebrationDate.isPresent()) {
            onNextDateLoaded(celebrationDate.get());
        } else {
            onNoEventsFound();
        }
    }

    abstract void onNextDateLoaded(ContactEventsOnADate events);

    abstract void onNoEventsFound();
}
