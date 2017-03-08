package com.alexstyl.specialdates.widgetprovider;

import android.os.AsyncTask;

import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.service.PeopleEventsProvider;

abstract class QueryUpcomingPeoplEventsTask extends AsyncTask<Void, Void, ContactEventsOnADate> {

    private final PeopleEventsProvider eventsProvider;

    QueryUpcomingPeoplEventsTask(PeopleEventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @Override
    protected ContactEventsOnADate doInBackground(Void... params) {
        Date today = Date.today();
        return eventsProvider.getCelebrationsClosestTo(today);
    }

    @Override
    protected void onPostExecute(ContactEventsOnADate celebrationDate) {
        super.onPostExecute(celebrationDate);
        onLoaded(celebrationDate);
    }

    abstract void onLoaded(ContactEventsOnADate celebrationDate);
}
