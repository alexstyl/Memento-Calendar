package com.alexstyl.specialdates.search;

import android.content.res.Resources;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

public class SearchHintCreator {

    private final Resources resources;
    private final NamedayPreferences namedayPreferences;

    public SearchHintCreator(Resources resources, NamedayPreferences namedayPreferences) {
        this.resources = resources;
        this.namedayPreferences = namedayPreferences;
    }

    public String createHint() {
        boolean enabled = namedayPreferences.isEnabled();
        if (enabled) {
            return resources.getString(R.string.search_hint_contacts_and_namedays);
        } else {
            return resources.getString(R.string.search_hint_contacts);
        }
    }
}
