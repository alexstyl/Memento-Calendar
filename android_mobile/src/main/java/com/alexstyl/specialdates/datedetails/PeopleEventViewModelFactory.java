package com.alexstyl.specialdates.datedetails;

import android.content.res.Resources;
import android.view.View;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.List;

final class PeopleEventViewModelFactory {

    private final Date todayDate;
    private final StringResources stringResources;
    private final Resources resources;

    PeopleEventViewModelFactory(Date todayDate, StringResources stringResources, Resources resources) {
        this.todayDate = todayDate;
        this.stringResources = stringResources;
        this.resources = resources;
    }

    List<DateDetailsViewModel> convertToViewModels(List<ContactEvent> events) {
        List<DateDetailsViewModel> models = new ArrayList<>();
        for (ContactEvent event : events) {
            models.add(new ContactEventViewModel(event.getContact(), event.getLabel(todayDate, stringResources), View.VISIBLE, resources.getColor(event.getType().getColorRes())));
        }
        return models;
    }

}
