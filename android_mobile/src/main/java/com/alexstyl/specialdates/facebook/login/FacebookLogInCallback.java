package com.alexstyl.specialdates.facebook.login;

import com.alexstyl.specialdates.facebook.UserCredentials;

interface FacebookLogInCallback {
    void onUserLoggedIn(UserCredentials loggedInCredentials);

    /**
     * Called when the user has successfully submitted their log in credentials.
     */
    void onUserCredentialsSubmitted();

    void onError(Exception e);
}
