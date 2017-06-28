package com.alexstyl.specialdates.facebook;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.images.ImageLoader;

class FacebookProfilePresenter {

    private static final FacebookImagePathCreator IMAGE_PATH = FacebookImagePathCreator.INSTANCE;

    private final FacebookLogoutService service;
    private final ImageView profilePicture;
    private final TextView userName;
    private final ImageLoader imageLoader;
    private final FacebookPreferences preferences;

    FacebookProfilePresenter(FacebookLogoutService service,
                             ImageView profilePicture,
                             TextView userName,
                             ImageLoader imageLoader,
                             FacebookPreferences preferences) {
        this.service = service;
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.imageLoader = imageLoader;
        this.preferences = preferences;
    }

    void startPresenting() {
        UserCredentials userCredentials = preferences.retrieveCredentials();

        userName.setText(userCredentials.getName());
        Uri uri = IMAGE_PATH.forUid(userCredentials.getUid());
        imageLoader.loadImage(uri, profilePicture);
    }

    void logOut() {
        service.logOut();
    }

    void stopPresenting() {
        service.dispose();
    }
}
