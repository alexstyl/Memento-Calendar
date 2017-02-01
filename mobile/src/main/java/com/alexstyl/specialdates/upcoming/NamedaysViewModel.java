package com.alexstyl.specialdates.upcoming;

import android.view.View;

import com.alexstyl.android.ViewVisibility;

public final class NamedaysViewModel {

    private final String namesLabel;
    @ViewVisibility
    private final int namedaysVisibility;
    private final int maxLines;

    NamedaysViewModel(String namesLabel, @ViewVisibility int namedaysVisibility, int maxLines) {
        this.namesLabel = namesLabel;
        this.namedaysVisibility = namedaysVisibility;
        this.maxLines = maxLines;
    }

    public String getNamesLabel() {
        return namesLabel;
    }

    @ViewVisibility
    public int getNamedaysVisibility() {
        return namedaysVisibility;
    }

    public int getMaxLines() {
        return maxLines;
    }

    boolean isHidden() {
        return namedaysVisibility == View.GONE;
    }
}
