package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

public class NamedaySettingsMonitor implements EventUpdatedMonitor {

    private final NamedayPreferences namedayPreferences;

    private boolean namedaysEnabled;
    private NamedayLocale displayingLanguage;
    private boolean namedayForContactsOnly;

    public NamedaySettingsMonitor(NamedayPreferences namedayPreferences) {
        this.namedayPreferences = namedayPreferences;
    }

    @Override
    public boolean dataWasUpdated() {
        return namedaysEnabled != namedayPreferences.isEnabled() ||
                namedayForContactsOnly != namedayPreferences.isEnabledForContactsOnly() ||
                !namedayPreferences.getSelectedLanguage().equals(displayingLanguage);
    }

    @Override
    public void refreshData() {
        namedaysEnabled = namedayPreferences.isEnabled();
        namedayForContactsOnly = namedayPreferences.isEnabledForContactsOnly();
        displayingLanguage = namedayPreferences.getSelectedLanguage();
    }

    @Override
    public void initialise() {
        namedaysEnabled = namedayPreferences.isEnabled();
        namedayForContactsOnly = namedayPreferences.isEnabledForContactsOnly();
        displayingLanguage = namedayPreferences.getSelectedLanguage();

    }
}
