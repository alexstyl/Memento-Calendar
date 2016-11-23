package com.alexstyl.specialdates.events.namedays;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class NamedayPreferences {

    private static final String DEFAULT_LOCALE = NamedayLocale.gr.name();

    private final boolean enabledByDefault;
    private final EasyPreferences preferences;

    public static NamedayPreferences newInstance(Context context) {
        boolean enabledByDefault = shouldNamedaysBeEnabledByDefault(context);

        return new NamedayPreferences(context, enabledByDefault);
    }

    private NamedayPreferences(Context context, boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
        this.preferences = EasyPreferences.createForDefaultPreferences(context);
    }

    public void setSelectedLanguage(String language) {
        preferences.setString(R.string.key_nameday_lang, language);
    }

    public NamedayLocale getSelectedLanguage() {
        String lang = preferences.getString(R.string.key_nameday_lang, DEFAULT_LOCALE);
        return NamedayLocale.from(lang);
    }

    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_enable_namedays, enabledByDefault);
    }

    public boolean isEnabledForContactsOnly() {
        return preferences.getBoolean(R.string.key_namedays_contacts_only, false);
    }

    public void setEnabledForContactsOnly(boolean onlyForContacts) {
        preferences.setBoolean(R.string.key_namedays_contacts_only, onlyForContacts);
    }

    public boolean shouldLookupAllNames() {
        return preferences.getBoolean(R.string.key_namedays_full_name, false);
    }

    private static boolean shouldNamedaysBeEnabledByDefault(Context context) {
        return context.getResources().getBoolean(R.bool.isNamedaySupported);
    }
}
