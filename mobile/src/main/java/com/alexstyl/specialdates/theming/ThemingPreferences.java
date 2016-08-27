package com.alexstyl.specialdates.theming;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.MementoApp;
import com.alexstyl.specialdates.R;

public class ThemingPreferences {

    private static final String DEFAULT_THEME = MementoTheme.CHERRY_RED.getThemeName();
    private final EasyPreferences preferences;

    public ThemingPreferences() {
        this.preferences = EasyPreferences.createForDefaultPreferences(MementoApp.getAppContext());
    }

    public MementoTheme getSelectedTheme() {
        String themeName = preferences.getString(R.string.key_app_theme, DEFAULT_THEME);
        return MementoTheme.fromName(themeName);
    }

    public void setSelectedTheme(MementoTheme selectedTheme) {
        preferences.setString(R.string.key_app_theme, selectedTheme.getThemeName());
    }
}
