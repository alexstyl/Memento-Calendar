package com.alexstyl.specialdates.facebook.login;

import com.alexstyl.specialdates.facebook.UserCredentials;

interface FacebookImportView {

    void showLoading();

    void showData(UserCredentials user);

    void showError();
}
