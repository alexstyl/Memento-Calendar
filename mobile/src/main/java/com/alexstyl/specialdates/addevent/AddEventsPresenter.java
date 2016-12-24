package com.alexstyl.specialdates.addevent;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Editable;
import android.view.View;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.AvatarCameraButtonView;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.OnImageLoadedCallback;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.meta.AndroidUtils;
import com.novoda.notils.text.SimpleTextWatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;

class AddEventsPresenter {

    private final ImageLoader imageLoader;
    private final ContactSuggestionView contactSuggestionView;
    private final AvatarCameraButtonView avatarView;
    private final ContactEventsFetcher contactEventsFetcher;
    private final ContactEventsAdapter adapter;
    private final ToolbarBackgroundAnimator toolbarAnimator;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory addEventFactory;
    private final ContactEventPersister contactEventPersister;
    private final MessageDisplayer messageDisplayer;

    private TemporaryEventsState state;

    AddEventsPresenter(ImageLoader imageLoader,
                       ContactEventsFetcher contactEventsFetcher,
                       ContactEventsAdapter adapter,
                       ContactSuggestionView contactSuggestionView,
                       AvatarCameraButtonView avatarView,
                       MementoToolbar toolbar,
                       ContactEventViewModelFactory factory,
                       AddEventViewModelFactory addEventFactory, ContactEventPersister contactEventPersister,
                       MessageDisplayer messageDisplayer) {
        this.imageLoader = imageLoader;
        this.contactSuggestionView = contactSuggestionView;
        this.avatarView = avatarView;
        this.contactEventsFetcher = contactEventsFetcher;
        this.adapter = adapter;
        this.toolbarAnimator = ToolbarBackgroundAnimator.setupOn(toolbar);
        this.factory = factory;
        this.addEventFactory = addEventFactory;
        this.contactEventPersister = contactEventPersister;
        this.messageDisplayer = messageDisplayer;
        this.state = TemporaryEventsState.newState();
    }

    void startPresenting() {
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.getContact().isPresent()) {
                    // retake image
                } else {
                    // take picture with camera
                }
            }
        });
        contactSuggestionView.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(final Contact contact) {
                state = TemporaryEventsState.forContact(contact);
                imageLoader.loadThumbnail(contact.getImagePath(), avatarView, new OnImageLoadedCallback() {
                    @Override
                    public void onImageLoaded(Bitmap loadedImage) {
                        avatarView.setImageBitmap(loadedImage);
                        toolbarAnimator.fadeOut();
                        AndroidUtils.requestHideKeyboard(contactSuggestionView.getContext(), contactSuggestionView);
                        contactSuggestionView.clearFocus();

                    }
                });
                contactEventsFetcher.load(contact, onNewContactLoadedCallback);
            }
        });

        contactSuggestionView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                TemporaryEventsState newState = state.asAnnonymous(s.toString());
                if (state.getContact().equals(newState.getContact())) {
                    // cary over the events
                    newState.keepEventsOf(state);
                }
                state = newState;
                avatarView.setImageBitmap(null); // TODO animate avatar out
                toolbarAnimator.fadeIn();
            }
        });
        contactEventsFetcher.loadEmptyEvents(onNewContactLoadedCallback);
    }

    private final ContactEventsFetcher.OnDataFetchedCallback onNewContactLoadedCallback = new ContactEventsFetcher.OnDataFetchedCallback() {

        @Override
        public void onDataFetched(List<ContactEventViewModel> data) {
            adapter.replace(data);
            state.keepState(data);
        }
    };

    void onEventDatePicked(EventType eventType, Date date) {
        state.keepState(eventType, date);
        ContactEventViewModel viewModels = factory.createViewModelWith(eventType, date);
        adapter.replace(viewModels);
    }

    void onEventRemoved(EventType eventType) {
        state.removeEvent(eventType);
        ContactEventViewModel viewModels = addEventFactory.createAddEventViewModelsFor(eventType);
        adapter.replace(viewModels);
    }

    void saveChanges() {
        if (state.getContact().isPresent()) {
            new AsyncTask<Object, Object, Boolean>() {
                @Override
                protected Boolean doInBackground(Object... params) {
                    Set<Map.Entry<EventType, Date>> events = state.getEvents();
                    Optional<Contact> contact = state.getContact();
                    return contactEventPersister.updateExistingContact(contact.get(), events);
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
                    return contactEventPersister.createNewContactWithEvents(state.getContactName(), state);
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
}
