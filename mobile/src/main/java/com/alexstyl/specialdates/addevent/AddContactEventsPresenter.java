package com.alexstyl.specialdates.addevent;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.DecodedImage;
import com.novoda.notils.meta.AndroidUtils;
import com.novoda.notils.text.SimpleTextWatcher;

class AddContactEventsPresenter {

    private final AvatarPresenter avatarPresenter;
    private final EventsPresenter eventsPresenter;
    private final ContactSuggestionView contactSuggestionView;
    private final ContactOperations contactOperations;
    private final MessageDisplayer messageDisplayer;
    private final ContactOperationsExecutor operationsExecutor;

    private SelectedContact selectedContact = SelectedContact.anonymous();

    AddContactEventsPresenter(AvatarPresenter avatarPresenter,
                              EventsPresenter eventsPresenter,
                              ContactSuggestionView contactSuggestionView,
                              ContactOperations contactOperations,
                              MessageDisplayer messageDisplayer,
                              ContactOperationsExecutor operationsExecutor) {
        this.avatarPresenter = avatarPresenter;
        this.eventsPresenter = eventsPresenter;
        this.contactSuggestionView = contactSuggestionView;
        this.contactOperations = contactOperations;
        this.messageDisplayer = messageDisplayer;
        this.operationsExecutor = operationsExecutor;
    }

    void startPresenting(final OnCameraClickedListener listener) {
        avatarPresenter.startPresenting(listener);
        eventsPresenter.startPresenting();
        contactSuggestionView.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(final Contact contact) {
                avatarPresenter.onContactSelected(contact);
                eventsPresenter.onContactSelected(contact);
                selectedContact = SelectedContact.forContact(contact);
                AndroidUtils.requestHideKeyboard(contactSuggestionView.getContext(), contactSuggestionView);
            }
        });
        contactSuggestionView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                if (selectedContact.containsContact()) {
                    avatarPresenter.removeAvatar();
                }
                selectedContact = SelectedContact.anonymous(text.toString());
            }
        });
    }

    void onEventDatePicked(EventType eventType, Date date) {
        eventsPresenter.onEventDatePicked(eventType, date);
    }

    void onEventRemoved(EventType eventType) {
        eventsPresenter.onEventRemoved(eventType);
    }

    void saveChanges() {
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
                        messageDisplayer.showMessage(R.string.add_birthday_contact_added);
                    } else {
                        messageDisplayer.showMessage(R.string.add_birthday_failed_to_add_contact);
                    }
                }
            }.execute();
        }
    }

    void presentAvatar(Uri photoUri) {
        avatarPresenter.presentAvatar(photoUri);
    }

    void removeAvatar() {
        avatarPresenter.removeAvatar();
    }
}
