package com.alexstyl.specialdates.facebook;

class FacebookProfilePresenter {
    private final FacebookLogoutService service;
    private final FacebookProfileView view;
    private final FacebookPreferences preferences;

    FacebookProfilePresenter(FacebookLogoutService service,
                             FacebookProfileView view,
                             FacebookPreferences preferences) {
        this.service = service;
        this.view = view;
        this.preferences = preferences;
    }

    void startPresenting() {
        UserCredentials userCredentials = preferences.retrieveCredentials();
        view.display(userCredentials);
    }

    void logOut() {
        service.logOut();
    }

    void stopPresenting() {
        service.dispose();
    }
}
