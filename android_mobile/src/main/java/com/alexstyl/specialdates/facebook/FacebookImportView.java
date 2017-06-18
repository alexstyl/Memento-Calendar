package com.alexstyl.specialdates.facebook;

interface FacebookImportView {

    void showLoading();

    void showData(UserCredentials user);

    void showError();
}
