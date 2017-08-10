package com.alexstyl.specialdates.search;

import android.content.res.Resources;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;

class SearchHintCreator {

    private final Resources resources;
    private final NamedayUserSettings namedayPreferences;

    SearchHintCreator(Resources resources, NamedayUserSettings namedayPreferences) {
        this.resources = resources;
        this.namedayPreferences = namedayPreferences;
    }

    String createHint() {
        boolean enabled = namedayPreferences.isEnabled();
        if (enabled) {
            return resources.getString(R.string.search_hint_contacts_and_namedays);
        } else {
            return resources.getString(R.string.search_hint_contacts);
        }
    }
}
