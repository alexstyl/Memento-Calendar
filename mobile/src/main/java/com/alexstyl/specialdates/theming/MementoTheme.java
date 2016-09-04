package com.alexstyl.specialdates.theming;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum MementoTheme {
    CHERRY_RED(withThemeName(R.string.theme_BloodyCherry), R.style.Theme_Memento_BloodyCherry),
    NAVY_BLUE(withThemeName(R.string.theme_NavyBlue), R.style.Theme_Memento_NavyBlue),
    GLOSS_PINK(withThemeName(R.string.theme_GlossPink), R.style.Theme_Memento_GlossPink),
    EGGPLANT_GREEN(withThemeName(R.string.theme_Eggplant), R.style.Theme_Memento_Eggplant),
    MONOCHROME(withThemeName(R.string.theme_Monochrome), R.style.Theme_Memento_Monochrome),
    SYSTEMO(withThemeName(R.string.theme_Systemo), R.style.Theme_Memento_Systemo),
    AMBER(withThemeName(R.string.theme_Amber), R.style.Theme_Memento_Amber),
    ;

    public static MementoTheme fromName(@NonNull String themeName) {
        for (MementoTheme mementoTheme : values()) {
            if (mementoTheme.getThemeName().equalsIgnoreCase(themeName)) {
                return mementoTheme;
            }
        }

        throw new DeveloperError("No theme exists with the name [%s]", themeName);
    }

    private static String withThemeName(@StringRes int themNameResId) {
        return MementoApplication.getAppContext().getString(themNameResId);
    }

    private final String themeName;
    @StyleRes
    private final int styleResId;

    MementoTheme(@NonNull String themeName, @StyleRes int styleResId) {
        this.themeName = themeName;
        this.styleResId = styleResId;
    }

    public String getThemeName() {
        return themeName;
    }

    @StyleRes
    public int androidTheme() {
        return styleResId;
    }

    @Override
    public String toString() {
        return themeName;
    }
}
