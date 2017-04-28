package com.alexstyl.specialdates.theming;

public final class ThemeMonitor {

    private final MementoTheme theme;
    private final ThemingPreferences preferences;

    public static ThemeMonitor startMonitoring(ThemingPreferences preferences) {
        MementoTheme currentTheme = preferences.getSelectedTheme();
        return new ThemeMonitor(currentTheme, preferences);
    }

    private ThemeMonitor(MementoTheme theme, ThemingPreferences preferences) {
        this.theme = theme;
        this.preferences = preferences;
    }

    public boolean hasThemeChanged() {
        MementoTheme newTheme = preferences.getSelectedTheme();
        return newTheme != theme;
    }
}
