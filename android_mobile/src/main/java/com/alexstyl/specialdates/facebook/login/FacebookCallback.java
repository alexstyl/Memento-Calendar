package com.alexstyl.specialdates.facebook.login;

import com.alexstyl.specialdates.facebook.UserCredentials;

interface FacebookCallback {
    void onCalendarFound(UserCredentials pageURL);

    void onError();

    void onSignedInThroughWebView();
}
