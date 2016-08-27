package com.alexstyl.specialdates.ui;

import android.content.Context;

import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.ThemingPreferences;

public class ThemeReapplier {

    private final Context context;

    private final MementoTheme theme;
    private final ThemingPreferences themingPreferences;

    public ThemeReapplier(Context context) {
        this.context = context.getApplicationContext();
        this.themingPreferences = new ThemingPreferences();
        this.theme = themingPreferences.getSelectedTheme();
    }

    public boolean hasThemeChanged() {
        MementoTheme newTheme = themingPreferences.getSelectedTheme();
        return newTheme != theme;
    }
}
