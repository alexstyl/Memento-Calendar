package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.ColorInt;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.contact.Contact;

public final class ContactEventViewModel {
    private final Contact contact;
    @ViewVisibility
    private int visibility;
    private final String contactName;
    private final String eventLabel;
    @ColorInt
    private final int eventColor;
    private final int backgroundVariant;
    private final Uri contactImagePath;
    private final Typeface typeface;

    public ContactEventViewModel(Contact contact,
                                 @ViewVisibility int visibility,
                                 String contactName,
                                 String eventLabel,
                                 @ColorInt int eventColor,
                                 int backgroundVariant,
                                 Uri contactImagePath,
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

    public Uri getContactImagePath() {
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
