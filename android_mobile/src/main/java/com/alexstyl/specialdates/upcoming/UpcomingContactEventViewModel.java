package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.contact.Contact;

import java.net.URI;

public final class UpcomingContactEventViewModel {
    private final Contact contact;
    @ViewVisibility
    private int visibility;
    private final String contactName;
    private final String eventLabel;
    @ColorInt
    private final int eventColor;
    private final int backgroundVariant;
    private final URI contactImagePath;
    private final Typeface typeface;

    UpcomingContactEventViewModel(Contact contact,
                                  @ViewVisibility int visibility,
                                  String contactName,
                                  String eventLabel,
                                  @ColorInt int eventColor,
                                  int backgroundVariant,
                                  URI contactImagePath,
                                  Typeface typeface
    ) {
        this.contact = contact;
        this.visibility = visibility;
        this.contactName = contactName;
        this.eventLabel = eventLabel;
        this.eventColor = eventColor;
        this.backgroundVariant = backgroundVariant;
        this.contactImagePath = contactImagePath;
        this.typeface = typeface;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    @ColorInt
    public int getEventColor() {
        return eventColor;
    }

    public int getBackgroundVariant() {
        return backgroundVariant;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public URI getContactImagePath() {
        return contactImagePath;
    }

    @ViewVisibility
    public int getContactEventVisibility() {
        return visibility;
    }

    public Contact getContact() {
        return contact;
    }
}
