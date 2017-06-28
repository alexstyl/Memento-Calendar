package com.alexstyl.specialdates.facebook.login;

import android.os.AsyncTask;

import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.facebook.UserCredentials;

import javax.security.auth.login.LoginException;

class UserCredentialsExtractorTask extends AsyncTask<Void, Void, UserCredentials> {

    private final CredentialsExtractor extractor = new CredentialsExtractor();

    private final String pageSource;
    private final FacebookLogInCallback callback;
    private final FacebookPreferences preferences;

    UserCredentialsExtractorTask(String pageSource, FacebookPreferences preferences, FacebookLogInCallback callback) {
        this.pageSource = pageSource;
        this.preferences = preferences;
        this.callback = callback;
    }

    @Override
    protected UserCredentials doInBackground(Void... params) {
        UserCredentials userCredentials = extractor.extractCalendarURL(pageSource);
        if (userCredentials != UserCredentials.ANNONYMOUS) {
            preferences.store(userCredentials);
        }
        return userCredentials;
    }

    @Override
    protected void onPostExecute(UserCredentials userCredentials) {
        if (userCredentials.isAnnonymous()) {
            callback.onError(new LoginException("Couldn't find extract calendar"));
        } else {
            callback.onUserLoggedIn(userCredentials);
        }

    }

}
