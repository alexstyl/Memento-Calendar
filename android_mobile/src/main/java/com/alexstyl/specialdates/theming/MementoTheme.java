package com.alexstyl.specialdates.theming;

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum MementoTheme {
    CHERRY_RED(0, R.string.theme_BloodyCherry, R.style.Theme_Memento_BloodyCherry),
    NAVY_BLUE(1, R.string.theme_NavyBlue, R.style.Theme_Memento_NavyBlue),
    GLOSS_PINK(2, R.string.theme_GlossPink, R.style.Theme_Memento_GlossPink),
    EGGPLANT_GREEN(3, R.string.theme_Eggplant, R.style.Theme_Memento_Eggplant),
    MONOCHROME(4, R.string.theme_Monochrome, R.style.Theme_Memento_Monochrome),
    SYSTEMO(5, R.string.theme_Systemo, R.style.Theme_Memento_Systemo),
    AMBER(6, R.string.theme_Amber, R.style.Theme_Memento_Amber);

    private int id;
    @StringRes
    private final int themeName;
    @StyleRes
    private final int styleResId;

    public static MementoTheme fromId(int themeId) {
        for (MementoTheme mementoTheme : values()) {
            if (mementoTheme.getId() == themeId) {
                return mementoTheme;
            }
        }

        throw new DeveloperError("No theme exists with the id [%s]", themeId);
    }

    MementoTheme(int id, @StringRes int themeName, @StyleRes int styleResId) {
        this.id = id;
        this.themeName = themeName;
        this.styleResId = styleResId;
    }

    @StringRes
    public int getThemeName() {
        return themeName;
    }

    public int getId() {
        return id;
    }

    @StyleRes
    public int androidTheme() {
        return styleResId;
    }
}
