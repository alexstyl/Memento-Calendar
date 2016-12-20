package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

import java.util.List;

class EventsListPresenter {

    private ImageLoader imageLoader;
    private final ContactSuggestionView contactSuggestionView;
    private final ColorImageView avatarView;
    private final ContactEventsFetcher contactEventsFetcher;
    private ContactEventsAdapter adapter;

    EventsListPresenter(ImageLoader imageLoader,
                        ContactEventsFetcher contactEventsFetcher,
                        ContactEventsAdapter adapter,
                        ContactSuggestionView contactSuggestionView,
                        ColorImageView avatarView) {
        this.imageLoader = imageLoader;
        this.contactSuggestionView = contactSuggestionView;
        this.avatarView = avatarView;
        this.contactEventsFetcher = contactEventsFetcher;
        this.adapter = adapter;
    }

    void startPresenting() {
        contactSuggestionView.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(Contact contact) {
                imageLoader.loadThumbnail(contact.getImagePath(), avatarView.getImageView());
                contactEventsFetcher.load(contact, new ContactEventsFetcher.OnDataFetched() {
                    @Override
                    public void onDataFetched(List<ContactEventViewModel> data) {
                        adapter.updateWith(data);
                    }
                });
            }
        });

    }
}
