package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class AndroidFacebookPreferences implements FacebookUserSettings {
    private final EasyPreferences preferences;

    AndroidFacebookPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void store(UserCredentials userCredentials) {
        preferences.setLong(R.string.key_facebook_user_id, userCredentials.getUid());
        preferences.setString(R.string.key_facebook_user_key, userCredentials.getKey());
        preferences.setString(R.string.key_facebook_user_name, userCredentials.getName());
    }

    @Override
    public UserCredentials retrieveCredentials() {
        long uid = preferences.getLong(R.string.key_facebook_user_id, -1);
        String key = preferences.getString(R.string.key_facebook_user_key, "");
        String name = preferences.getString(R.string.key_facebook_user_name, "");
        return new UserCredentials(uid, key, name);
    }

    @Override
    public boolean isLoggedIn() {
        return !UserCredentials.ANNONYMOUS.equals(retrieveCredentials());
    }
}
