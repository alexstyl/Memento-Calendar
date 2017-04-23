package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider;

import java.util.List;

class UpcomingEventsLoader extends SimpleAsyncTaskLoader<List<UpcomingRowViewModel>> {

    private final Date startingPeriod;
    private final UpcomingEventsProvider eventsProvider;

    UpcomingEventsLoader(Context context,
                         Date startingPeriod,
                         UpcomingEventsProvider eventsProvider) {
        super(context);
        this.startingPeriod = startingPeriod;
        this.eventsProvider = eventsProvider;
    }

    @Override
    public List<UpcomingRowViewModel> loadInBackground() {
        TimePeriod timePeriod = TimePeriod.between(
                startingPeriod,
                startingPeriod.addDay(364)
        );
        return eventsProvider.calculateEventsBetween(timePeriod);
    }

}
