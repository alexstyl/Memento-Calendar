package com.alexstyl.specialdates.facebook

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R

class AndroidFacebookPreferences(private val preferences: EasyPreferences) : FacebookUserSettings {

    override val isLoggedIn: Boolean
        get() = UserCredentials.ANONYMOUS != retrieveCredentials()

    override fun store(userCredentials: UserCredentials) {
        preferences.setLong(R.string.key_facebook_user_id, userCredentials.uid)
        preferences.setString(R.string.key_facebook_user_key, userCredentials.key)
        preferences.setString(R.string.key_facebook_user_name, userCredentials.name)
    }

    override fun retrieveCredentials(): UserCredentials {
        val uid = preferences.getLong(R.string.key_facebook_user_id, -1)
        val key = preferences.getString(R.string.key_facebook_user_key, "")
        val name = preferences.getString(R.string.key_facebook_user_name, "")
        return UserCredentials(uid, key, name)
    }
}
