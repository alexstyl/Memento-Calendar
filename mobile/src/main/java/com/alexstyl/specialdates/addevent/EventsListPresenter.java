package com.alexstyl.specialdates.addevent;

import android.graphics.Bitmap;
import android.text.Editable;
import android.widget.ImageView;

import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.OnImageLoadedCallback;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.logger.simple.Log;
import com.novoda.notils.meta.AndroidUtils;
import com.novoda.notils.text.SimpleTextWatcher;

import java.util.List;

class EventsListPresenter {

    private final ImageLoader imageLoader;
    private final ContactSuggestionView contactSuggestionView;
    private final ImageView avatarView;
    private final ContactEventsFetcher contactEventsFetcher;
    private final ContactEventsAdapter adapter;
    private final ToolbarBackgroundAnimator toolbarAnimator;

    private TemporaryEventsState state;

    EventsListPresenter(ImageLoader imageLoader,
                        ContactEventsFetcher contactEventsFetcher,
                        ContactEventsAdapter adapter,
                        ContactSuggestionView contactSuggestionView,
                        ImageView avatarView,
                        MementoToolbar toolbar) {
        this.imageLoader = imageLoader;
        this.contactSuggestionView = contactSuggestionView;
        this.avatarView = avatarView;
        this.contactEventsFetcher = contactEventsFetcher;
        this.adapter = adapter;
        this.toolbarAnimator = ToolbarBackgroundAnimator.setupOn(toolbar);
        this.state = TemporaryEventsState.newState();
    }

    void startPresenting() {
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
                contactEventsFetcher.loadEmptyEvents(onNewContactLoadedCallback);
                avatarView.setImageBitmap(null); // TODO animate avatar out
                toolbarAnimator.fadeIn();
            }
        });
        contactEventsFetcher.loadEmptyEvents(onNewContactLoadedCallback);
    }

    private final ContactEventsFetcher.OnDataFetchedCallback onNewContactLoadedCallback = new ContactEventsFetcher.OnDataFetchedCallback() {

        @Override
        public void onDataFetched(List<ContactEventViewModel> data) {
            adapter.updateWith(data);
            state.keepState(data);
        }
    };

    void onEventDatePicked(EventType eventType, Date date) {
        state.keepState(eventType, date);
    }

    void saveChanges() {
        printState();
    }

    private void printState() {
        Log.d(state);
    }
}
