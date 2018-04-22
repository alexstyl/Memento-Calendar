package com.alexstyl.specialdates.addevent;

import android.os.AsyncTask;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.DecodedImage;
import com.novoda.notils.logger.simple.Log;

import java.net.URI;

public class AddContactEventsPresenter {

    private final Analytics analytics;
    private final AvatarPresenter avatarPresenter;
    private final EventsPresenter eventsPresenter;
    private final ContactOperations contactOperations;
    private final MessageDisplayer messageDisplayer;
    private final ContactOperationsExecutor operationsExecutor;

    private SelectedContact selectedContact = SelectedContact.anonymous();
    private boolean modified = false;

    AddContactEventsPresenter(Analytics analytics,
                              AvatarPresenter avatarPresenter,
                              EventsPresenter eventsPresenter,
                              ContactOperations contactOperations,
                              MessageDisplayer messageDisplayer,
                              ContactOperationsExecutor operationsExecutor) {
        this.analytics = analytics;
        this.avatarPresenter = avatarPresenter;
        this.eventsPresenter = eventsPresenter;
        this.contactOperations = contactOperations;
        this.messageDisplayer = messageDisplayer;
        this.operationsExecutor = operationsExecutor;
    }

    void startPresenting(OnCameraClickedListener listener) {
        avatarPresenter.startPresenting(listener);
        eventsPresenter.startPresenting();
    }

    void onContactSelected(Contact contact) {
        analytics.trackContactSelected();
        avatarPresenter.onContactSelected(contact);
        eventsPresenter.onContactSelected(contact);
        selectedContact = SelectedContact.forContact(contact);
        modified = false;
    }

    void onEventDatePicked(EventType eventType, Date date) {
        analytics.trackEventDatePicked(eventType);
        eventsPresenter.onEventDatePicked(eventType, date);
        modified = true;
    }

    void removeEvent(EventType eventType) {
        analytics.trackEventRemoved(eventType);
        eventsPresenter.removeEvent(eventType);
        modified = true;
    }

    void onNameModified(String text) {
        if (selectedContact.containsContact()) {
            avatarPresenter.removeAvatar();
        }
        selectedContact = SelectedContact.anonymous(text);
        modified = true;
    }

    void presentAvatar(URI photoUri) {
        analytics.trackAvatarSelected();
        avatarPresenter.presentAvatar(photoUri);
        modified = true;
    }

    void removeAvatar() {
        analytics.trackEventAddedSuccessfully();
        avatarPresenter.removeAvatar();
        modified = true;
    }

    boolean displaysAvatar() {
        return avatarPresenter.getDecodedImage() != DecodedImage.EMPTY;
    }

    boolean isHoldingModifiedData() {
        return modified;
    }

    void saveChanges() {
        if (!modified) {
            Log.w("Nothing to save. Bailing fast");
            return;
        }
        if (selectedContact.containsContact()) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    Contact contact = selectedContact.getContact();

                    ContactOperations.ContactOperationsBuilder builder =
                            contactOperations.updateExistingContact(contact)
                                    .withEvents(eventsPresenter.getEvents())
                                    .updateContactImage(avatarPresenter.getDecodedImage());
                    return operationsExecutor.execute(builder.build());
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        analytics.trackContactUpdated();
                        messageDisplayer.showMessage(R.string.add_event_contact_updated);
                    } else {
                        messageDisplayer.showMessage(R.string.add_event_failed_to_update_contact);
                    }

                }
            }.execute();
        } else {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {

                    ContactOperations.ContactOperationsBuilder builder =
                            contactOperations.createNewContact(selectedContact.getDisplayName())
                                    .withEvents(eventsPresenter.getEvents());
                    DecodedImage decodedImage = avatarPresenter.getDecodedImage();
                    if (decodedImage != DecodedImage.EMPTY) {
                        builder.addContactImage(decodedImage);
                    }
                    return operationsExecutor.execute(builder.build());
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        analytics.trackContactCreated();
                        messageDisplayer.showMessage(R.string.add_birthday_contact_added);
                    } else {
                        messageDisplayer.showMessage(R.string.add_birthday_failed_to_add_contact);
                    }
                }
            }.execute();
        }
    }
}
