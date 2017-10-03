package com.alexstyl.specialdates.events.namedays;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class NamedayPreferences implements NamedayUserSettings {

    private static final String DEFAULT_LOCALE = NamedayLocale.GREEK.getCountryCode();

    private final boolean enabledByDefault;
    private final EasyPreferences preferences;

    NamedayPreferences(Context context) {
        this.preferences = EasyPreferences.createForDefaultPreferences(context);
        this.enabledByDefault = shouldNamedaysBeEnabledByDefault(context);
    }

    @Override
    public void setSelectedLanguage(String language) {
        preferences.setString(R.string.key_nameday_lang, language);
    }

    @Override
    public NamedayLocale getSelectedLanguage() {
        String lang = preferences.getString(R.string.key_nameday_lang, DEFAULT_LOCALE);
        return NamedayLocale.from(lang);
    }

    @Override
    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_enable_namedays, enabledByDefault);
    }

    @Override
    public boolean isEnabledForContactsOnly() {
        return preferences.getBoolean(R.string.key_namedays_contacts_only, false);
    }

    @Override
    public void setEnabledForContactsOnly(boolean onlyForContacts) {
        preferences.setBoolean(R.string.key_namedays_contacts_only, onlyForContacts);
    }

    @Override
    public boolean shouldLookupAllNames() {
        return preferences.getBoolean(R.string.key_namedays_full_name, false);
    }

    private static boolean shouldNamedaysBeEnabledByDefault(Context context) {
        return context.getResources().getBoolean(R.bool.isNamedaySupported);
    }
}
