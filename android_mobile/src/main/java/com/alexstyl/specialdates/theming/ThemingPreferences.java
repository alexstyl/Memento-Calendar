package com.alexstyl.specialdates.theming;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class ThemingPreferences {

    private static final String DEFAULT_THEME = MementoTheme.CHERRY_RED.getThemeName();
    private final EasyPreferences preferences;

    public static ThemingPreferences newInstance(Context context) {
        return new ThemingPreferences(EasyPreferences.createForDefaultPreferences(context));
    }

    private ThemingPreferences(EasyPreferences defaultPreferences) {
        this.preferences = defaultPreferences;
    }

    public MementoTheme getSelectedTheme() {
        String themeName = preferences.getString(R.string.key_app_theme, DEFAULT_THEME);
        return MementoTheme.fromName(themeName);
    }

    public void setSelectedTheme(MementoTheme selectedTheme) {
        preferences.setString(R.string.key_app_theme, selectedTheme.getThemeName());
    }
}
