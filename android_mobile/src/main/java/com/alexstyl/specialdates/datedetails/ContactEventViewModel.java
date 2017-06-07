package com.alexstyl.specialdates.datedetails;

import android.support.annotation.ColorRes;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.contact.Contact;

class ContactEventViewModel implements DateDetailsViewModel {

    private final Contact contact;
    private final String eventLabel;
    @ViewVisibility
    private int eventLabelVisibility;
    @ColorRes
    private final int eventLabelColor;

    ContactEventViewModel(Contact contact, String eventLabel, @ViewVisibility int eventLabelVisibility, @ColorRes int eventLabelColor) {
        this.contact = contact;
        this.eventLabel = eventLabel;
        this.eventLabelVisibility = eventLabelVisibility;
        this.eventLabelColor = eventLabelColor;
    }

    public Contact getContact() {
        return contact;
    }

    @ColorRes
    public int getEventLabelColor() {
        return eventLabelColor;
    }

    @ViewVisibility
    public int getEventLabelVisibility() {
        return eventLabelVisibility;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    @Override
    public int getViewType() {
        return DateDetailsViewType.CONTACT_EVENT;
    }
}
