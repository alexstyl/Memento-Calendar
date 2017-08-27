package com.alexstyl.specialdates.settings;

import android.content.res.Resources;

import com.alexstyl.specialdates.theming.MementoTheme;

import java.util.Comparator;

class MementoThemeNameComparator implements Comparator<MementoTheme> {
    private Resources resources;

    MementoThemeNameComparator(Resources resources) {
        this.resources = resources;
    }

    @Override
    public int compare(MementoTheme first, MementoTheme second) {
        return nameOf(first).compareTo(nameOf(second));
    }

    private String nameOf(MementoTheme theme) {
        return resources.getString(theme.getThemeName());
    }
}
