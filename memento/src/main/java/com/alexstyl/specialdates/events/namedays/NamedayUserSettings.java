package com.alexstyl.specialdates.events.namedays;

public interface NamedayUserSettings {
    void setSelectedLanguage(String language);

    NamedayLocale getSelectedLanguage();

    boolean isEnabled();

    boolean isEnabledForContactsOnly();

    void setEnabledForContactsOnly(boolean onlyForContacts);

    boolean shouldLookupAllNames();
}
