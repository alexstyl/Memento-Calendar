package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;
import android.view.View;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;

final class ContactViewModelFactory {
    private final ColorResources colorResources;
    private final StringResources stringResources;

    ContactViewModelFactory(ColorResources colorResources, StringResources stringResources) {
        this.colorResources = colorResources;
        this.stringResources = stringResources;
    }

    ContactEventViewModel createViewModelFor(Typeface typeface, ContactEvent contactEvent) {
        Contact contact = contactEvent.getContact();
        return new ContactEventViewModel(
                contact, View.VISIBLE,
                contact.getDisplayName().toString(),
                contactEvent.getLabel(stringResources),
                colorResources.getColor(contactEvent.getType().getColorRes()),
                ((int) contact.getContactID()),
                contact.getImagePath(),
                typeface
        );
    }

}
