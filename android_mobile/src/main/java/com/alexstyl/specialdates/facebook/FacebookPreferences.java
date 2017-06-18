package com.alexstyl.specialdates.facebook;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class FacebookPreferences {
    private final EasyPreferences preferences;

    public static FacebookPreferences newInstance(Context context) {
        return new FacebookPreferences(EasyPreferences.createForPrivatePreferences(context, R.string.pref_facebook));
    }

    private FacebookPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    public void store(UserCredentials userCredentials) {
        preferences.setLong(R.string.key_facebook_user_id, userCredentials.getUid());
        preferences.setString(R.string.key_facebook_user_key, userCredentials.getKey());
        preferences.setString(R.string.key_facebook_user_name, userCredentials.getName());
    }

    public UserCredentials retrieveCredentials() {
        long uid = preferences.getLong(R.string.key_facebook_user_id, -1);
        String key = preferences.getString(R.string.key_facebook_user_key, "");
        String name = preferences.getString(R.string.key_facebook_user_name, "");
        return new UserCredentials(uid, key, name);
    }
}
