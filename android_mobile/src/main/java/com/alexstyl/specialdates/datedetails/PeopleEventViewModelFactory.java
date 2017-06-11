package com.alexstyl.specialdates.datedetails;

import android.content.res.Resources;
import android.view.View;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.actions.ContactActionFactory;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;

import java.util.ArrayList;
import java.util.List;

final class PeopleEventViewModelFactory {

    private final Date todayDate;
    private final StringResources stringResources;
    private final Resources resources;
    private final ContactActionFactory factory;

    PeopleEventViewModelFactory(Date todayDate, StringResources stringResources, Resources resources, ContactActionFactory factory) {
        this.todayDate = todayDate;
        this.stringResources = stringResources;
        this.resources = resources;
        this.factory = factory;
    }

    List<DateDetailsViewModel> convertToViewModels(List<ContactEvent> events) {
        List<DateDetailsViewModel> models = new ArrayList<>();
        for (ContactEvent event : events) {
            Contact contact = event.getContact();
            List<LabeledAction> actions = factory.createActionsFor(contact);
            int actionsVisibility = actions.isEmpty() ? View.GONE : View.VISIBLE;
            models.add(
                    new ContactEventViewModel(
                            contact,
                            event.getLabel(todayDate, stringResources),
                            View.VISIBLE,
                            resources.getColor(event.getType().getColorRes()),
                            actionsVisibility,
                            actions
                    )
            );
        }
        return models;
    }

}
