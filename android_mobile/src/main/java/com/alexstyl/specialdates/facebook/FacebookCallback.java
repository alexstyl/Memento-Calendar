package com.alexstyl.specialdates.facebook;

interface FacebookCallback {
    void onCalendarFound(UserCredentials pageURL);

    void onError();

    void onSignedInThroughWebView();
}
