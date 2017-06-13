package com.alexstyl.specialdates.datedetails;

import com.alexstyl.specialdates.events.namedays.NamesInADate;

final class NamedayViewModelFactory {
    DateDetailsViewModel convertToViewModels(NamesInADate namedays) {
        return new NamedaysViewModel(namedays);
    }
}
