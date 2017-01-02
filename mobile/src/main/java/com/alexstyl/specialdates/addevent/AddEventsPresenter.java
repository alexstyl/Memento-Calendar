package com.alexstyl.specialdates.addevent;

import android.graphics.Bitmap;
import android.net.Uri;
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
import com.alexstyl.specialdates.images.DecodedImage;
import com.alexstyl.specialdates.images.ImageDecoder;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.OnImageLoadedCallback;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.meta.AndroidUtils;
import com.novoda.notils.text.SimpleTextWatcher;

import java.util.List;

class AddEventsPresenter {

    private final ImageLoader imageLoader;
    private final ContactSuggestionView contactSuggestionView;
    private final AvatarCameraButtonView avatarView;
    private final ContactEventsFetcher contactEventsFetcher;
    private final ContactEventsAdapter adapter;
    private final ToolbarBackgroundAnimator toolbarAnimator;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory addEventFactory;
    private final ContactOperations contactOperations;
    private final MessageDisplayer messageDisplayer;
    private final ContactOperationsExecutor operationsExecutor;
    private final ImageDecoder imageDecoder;

    private TemporaryEventsState state;
    private Optional<Bitmap> currentImageLoaded = Optional.absent();
    private final OnImageLoadedCallback onImageLoadedCallback = new OnImageLoadedCallback() {
        @Override
        public void onImageLoaded(Bitmap loadedImage) {
            currentImageLoaded = new Optional<>(loadedImage);
            avatarView.setImageBitmap(loadedImage);
            toolbarAnimator.fadeOut();
            avatarView.requestFocus();
        }
    };

    AddEventsPresenter(ImageLoader imageLoader,
                       ContactEventsFetcher contactEventsFetcher,
                       ContactEventsAdapter adapter,
                       ContactSuggestionView contactSuggestionView,
                       AvatarCameraButtonView avatarView,
                       MementoToolbar toolbar,
                       ContactEventViewModelFactory factory,
                       AddEventViewModelFactory addEventFactory,
                       ContactOperations contactOperations,
                       MessageDisplayer messageDisplayer, ContactOperationsExecutor operationsExecutor,
                       ImageDecoder imageDecoder) {
        this.imageLoader = imageLoader;
        this.contactSuggestionView = contactSuggestionView;
        this.avatarView = avatarView;
        this.contactEventsFetcher = contactEventsFetcher;
        this.adapter = adapter;
        this.toolbarAnimator = ToolbarBackgroundAnimator.setupOn(toolbar);
        this.factory = factory;
        this.addEventFactory = addEventFactory;
        this.contactOperations = contactOperations;
        this.messageDisplayer = messageDisplayer;
        this.operationsExecutor = operationsExecutor;
        this.imageDecoder = imageDecoder;
        this.state = TemporaryEventsState.newState();
    }

    void startPresenting(final OnCameraClickedListener listener) {
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request permission
                if (avatarView.isDisplayingAvatar()) {
                    listener.onPictureRetakenRequested();
                } else {
                    listener.onNewPictureTakenRequested();
                }
            }
        });
        contactSuggestionView.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(final Contact contact) {
                state = TemporaryEventsState.forContact(contact);
                imageLoader.loadImage(contact.getImagePath(), avatarView, onImageLoadedCallback);
                AndroidUtils.requestHideKeyboard(contactSuggestionView.getContext(), contactSuggestionView);
                contactEventsFetcher.load(contact, onNewContactLoadedCallback);
            }
        });
        contactSuggestionView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                TemporaryEventsState newState = state.asAnnonymous(text.toString());
                if (hasModifiedSelectedContact()) {
                    removeAvatar();
                } else {
                    newState.keepEventsOf(state);
                }
                state = newState;
            }

            private boolean hasModifiedSelectedContact() {
                return state.getContact().isPresent();
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
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    Contact contact = state.getContact().get();

                    ContactOperations.ContactOperationsBuilder builder =
                            contactOperations.updateExistingContact(contact)
                                    .withEvents(state.getEvents())
                                    .updateContactImage(getDecodedImage());
                    return operationsExecutor.execute(builder.build());
                }

                DecodedImage getDecodedImage() {
                    return currentImageLoaded.isPresent()
                            ? imageDecoder.decodeFrom(currentImageLoaded.get())
                            : DecodedImage.EMPTY;
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
                            contactOperations.createNewContact(state.getContactName())
                                    .withEvents(state.getEvents());

                    if (currentImageLoaded.isPresent()) {
                        DecodedImage image = imageDecoder.decodeFrom(currentImageLoaded.get());
                        builder.addContactImage(image);
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

    void presentAvatar(final Uri imageUri) {
        imageLoader.loadImage(imageUri, avatarView, onImageLoadedCallback);
    }

    void removeAvatar() {
        avatarView.setImageBitmap(null); // TODO animate currentImageLoaded out
        currentImageLoaded = Optional.absent();
        toolbarAnimator.fadeIn();
    }
}
