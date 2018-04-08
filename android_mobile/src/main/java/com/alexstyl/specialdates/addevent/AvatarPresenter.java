package com.alexstyl.specialdates.addevent;

import android.graphics.Bitmap;
import android.view.View;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.DecodedImage;
import com.alexstyl.specialdates.images.ImageDecoder;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.ImageLoadedConsumer;
import com.alexstyl.specialdates.images.SimpleImageLoadedConsumer;

import java.net.URI;

final class AvatarPresenter {

    private final ImageLoader imageLoader;
    private final AvatarPickerView avatarView;
    private final ToolbarBackgroundAnimator toolbarAnimator;
    private final ImageDecoder imageDecoder;

    private Optional<Bitmap> currentImageLoaded = Optional.Companion.absent();

    AvatarPresenter(ImageLoader imageLoader,
                    AvatarPickerView avatarView,
                    ToolbarBackgroundAnimator toolbarAnimator,
                    ImageDecoder imageDecoder) {
        this.imageLoader = imageLoader;
        this.avatarView = avatarView;
        this.toolbarAnimator = toolbarAnimator;
        this.imageDecoder = imageDecoder;
    }

    void onContactSelected(Contact contact) {
        imageLoader
                .load(contact.getImagePath())
                .withSize(avatarView.getWidth(), avatarView.getHeight())
                .into(imageLoadedConsumer);
    }

    private final ImageLoadedConsumer imageLoadedConsumer = new SimpleImageLoadedConsumer() {
        @Override
        public void onImageLoaded(Bitmap loadedImage) {
            currentImageLoaded = new Optional<>(loadedImage);
            avatarView.setImageBitmap(loadedImage);
            toolbarAnimator.fadeOut();
            avatarView.requestFocus();
        }

        @Override
        public void onLoadingFailed() {
            removeAvatar();
        }
    };

    void startPresenting(final OnCameraClickedListener listener) {
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avatarView.isDisplayingAvatar()) {
                    listener.onPictureRetakenRequested();
                } else {
                    listener.onNewPictureTakenRequested();
                }
            }
        });
    }

    void presentAvatar(URI imageUri) {
        imageLoader
                .load(imageUri)
                .withSize(avatarView.getWidth(), avatarView.getHeight())
                .into(imageLoadedConsumer);

    }

    void removeAvatar() {
        avatarView.setImageBitmap(null);
        currentImageLoaded = Optional.Companion.absent();
        toolbarAnimator.fadeIn();
    }

    DecodedImage getDecodedImage() {
        return currentImageLoaded.isPresent()
                ? imageDecoder.decodeFrom(currentImageLoaded.get())
                : DecodedImage.EMPTY;
    }
}
