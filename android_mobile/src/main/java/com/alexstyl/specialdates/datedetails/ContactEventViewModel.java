package com.alexstyl.specialdates.datedetails;

import android.support.annotation.ColorInt;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;

import java.util.List;

class ContactEventViewModel implements DateDetailsViewModel {

    private final Contact contact;
    private final String eventLabel;
    @ViewVisibility
    private int eventLabelVisibility;
    @ColorInt
    private final int eventLabelColor;
    @ViewVisibility
    private int actionsVisibility;
    private final List<LabeledAction> actions;

    ContactEventViewModel(Contact contact,
                          String eventLabel,
                          @ViewVisibility int eventLabelVisibility,
                          @ColorInt int eventLabelColor,
                          @ViewVisibility int actionsVisibility,
                          List<LabeledAction> actions) {
        this.contact = contact;
        this.eventLabel = eventLabel;
        this.eventLabelVisibility = eventLabelVisibility;
        this.eventLabelColor = eventLabelColor;
        this.actionsVisibility = actionsVisibility;
        this.actions = actions;
    }

    public Contact getContact() {
        return contact;
    }

    @ColorInt
    int getEventLabelColor() {
        return eventLabelColor;
    }

    @ViewVisibility
    int getEventLabelVisibility() {
        return eventLabelVisibility;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    @Override
    public int getViewType() {
        return DateDetailsViewType.CONTACT_EVENT;
    }

    @ViewVisibility
    int getActionsVisibility() {
        return actionsVisibility;
    }

    List<LabeledAction> getActions() {
        return actions;
    }
}
