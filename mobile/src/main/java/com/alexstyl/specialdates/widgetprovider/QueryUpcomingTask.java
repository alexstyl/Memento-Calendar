package com.alexstyl.specialdates.widgetprovider;

import android.os.AsyncTask;

import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.service.PeopleEventsProvider;

public abstract class QueryUpcomingTask extends AsyncTask<Void, Void, ContactEvents> {

    private final PeopleEventsProvider eventsProvider;

    protected QueryUpcomingTask(PeopleEventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @Override
    protected ContactEvents doInBackground(Void... params) {
        DayDate today = DayDate.today();
        return eventsProvider.getCelebrationsClosestTo(today);
    }

    @Override
    protected void onPostExecute(ContactEvents celebrationDate) {
        super.onPostExecute(celebrationDate);
        onLoaded(celebrationDate);
    }

    abstract void onLoaded(ContactEvents celebrationDate);
}
