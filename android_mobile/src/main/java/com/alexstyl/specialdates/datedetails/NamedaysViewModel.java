package com.alexstyl.specialdates.datedetails;

import com.alexstyl.specialdates.events.namedays.NamesInADate;

final class NamedaysViewModel implements DateDetailsViewModel {

    private final NamesInADate namedays;

    NamedaysViewModel(NamesInADate namedays) {
        this.namedays = namedays;
    }

    public NamesInADate getNamedays() {
        return namedays;
    }

    @Override
    @DateDetailsViewType
    public int getViewType() {
        return DateDetailsViewType.NAMEDAY;
    }
}
