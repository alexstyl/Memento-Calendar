package com.alexstyl.specialdates.facebook;

class FacebookProfilePresenter {
    private final FacebookLogoutService service;
    private final FacebookProfileView view;
    private final FacebookUserSettings settings;

    FacebookProfilePresenter(FacebookLogoutService service,
                             FacebookProfileView view,
                             FacebookUserSettings settings) {
        this.service = service;
        this.view = view;
        this.settings = settings;
    }

    void startPresenting() {
        UserCredentials userCredentials = settings.retrieveCredentials();
        view.display(userCredentials);
    }

    void logOut() {
        service.logOut();
    }

    void stopPresenting() {
        service.dispose();
    }
}
