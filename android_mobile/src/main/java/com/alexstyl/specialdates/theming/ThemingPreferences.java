package com.alexstyl.specialdates.theming;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class ThemingPreferences {

    private static final MementoTheme DEFAULT_THEME = MementoTheme.CHERRY_RED;

    private final EasyPreferences preferences;

    public static ThemingPreferences newInstance(Context context) {
        return new ThemingPreferences(EasyPreferences.createForDefaultPreferences(context));
    }

    private ThemingPreferences(EasyPreferences defaultPreferences) {
        this.preferences = defaultPreferences;
    }

    public MementoTheme getSelectedTheme() {
        int themeId = preferences.getInt(R.string.key_app_theme_id, DEFAULT_THEME.getId());
        return MementoTheme.fromId(themeId);
    }

    public void setSelectedTheme(MementoTheme selectedTheme) {
        preferences.setInteger(R.string.key_app_theme_id, selectedTheme.getId());
    }
}
