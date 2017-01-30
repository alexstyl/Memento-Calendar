package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

public final class NamedaySettingsMonitor implements EventUpdatedMonitor {

    private final NamedayPreferences namedayPreferences;

    private boolean namedaysEnabled;
    private NamedayLocale displayingLanguage;
    private boolean namedayForContactsOnly;
    private boolean namedayFullName;

    public NamedaySettingsMonitor(NamedayPreferences namedayPreferences) {
        this.namedayPreferences = namedayPreferences;
    }

    @Override
    public boolean dataWasUpdated() {
        return namedaysEnabled != namedayPreferences.isEnabled() ||
                namedayForContactsOnly != namedayPreferences.isEnabledForContactsOnly() ||
                !namedayPreferences.getSelectedLanguage().equals(displayingLanguage) ||
                namedayFullName != namedayPreferences.shouldLookupAllNames();
    }

    @Override
    public void initialise() {
        refreshData();
    }

    @Override
    public void refreshData() {
        namedaysEnabled = namedayPreferences.isEnabled();
        namedayForContactsOnly = namedayPreferences.isEnabledForContactsOnly();
        displayingLanguage = namedayPreferences.getSelectedLanguage();
        namedayFullName = namedayPreferences.shouldLookupAllNames();
    }
}
