package com.alexstyl.specialdates.contact;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.date.Date;

import java.util.List;

public abstract class Contact {

    protected final long contactID;
    protected final DisplayName displayName;
    private final Optional<Date> dateOfBirth;
    private List<LabeledAction> actions;

    public Contact(long id, DisplayName displayName, Optional<Date> dateOfBirth) {
        this.contactID = id;
        this.displayName = displayName;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return displayName.toString();
    }

    public long getContactID() {
        return contactID;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public String getGivenName() {
        return displayName.getFirstNames().getPrimary();
    }

    /**
     * Returns the actions for this contact
     */
    public List<LabeledAction> getUserActions(Context context) {
        if (actions == null) {
            actions = onBuildActions(context);
        }
        return actions;
    }

    /**
     * Creates the actions that can be performed on this contact's phones, emails etc.
     */
    protected abstract List<LabeledAction> onBuildActions(Context context);

    public Date getDateOfBirth() {
        return dateOfBirth.get();
    }

    public boolean hasDateOfBirth() {
        return dateOfBirth.isPresent();
    }

    public abstract Uri getLookupUri();

    /**
     * Displays the contact information of this contact
     */
    public abstract void displayQuickInfo(Context context, View view);

    /**
     * Returns the image path of the avatar of this contact
     */
    public abstract String getImagePath();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;

        return contactID == contact.contactID;

    }

    @Override
    public int hashCode() {
        return (int) (contactID ^ (contactID >>> 32));
    }
}
