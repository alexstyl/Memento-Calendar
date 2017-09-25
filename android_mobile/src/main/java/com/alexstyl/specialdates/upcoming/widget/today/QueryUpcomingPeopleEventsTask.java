package com.alexstyl.specialdates.upcoming.widget.today;

import android.os.AsyncTask;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.service.PeopleEventsProvider;

abstract class QueryUpcomingPeopleEventsTask extends AsyncTask<Object, Object, Optional<ContactEventsOnADate>> {

    private final PeopleEventsProvider eventsProvider;

    QueryUpcomingPeopleEventsTask(PeopleEventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @Override
    protected Optional<ContactEventsOnADate> doInBackground(Object... params) {
        Date today = Date.Companion.today();
        return eventsProvider.getCelebrationsClosestTo(today);
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
