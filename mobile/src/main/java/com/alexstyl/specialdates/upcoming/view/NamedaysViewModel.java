package com.alexstyl.specialdates.upcoming.view;

import com.alexstyl.android.ViewVisibility;

public final class NamedaysViewModel {

    private final String namesLabel;
    @ViewVisibility
    private final int namedaysVisibility;
    private final int maxLines;

    public NamedaysViewModel(String namesLabel, @ViewVisibility int namedaysVisibility, int maxLines) {
        this.namesLabel = namesLabel;
        this.namedaysVisibility = namedaysVisibility;
        this.maxLines = maxLines;
    }

    String getNamesLabel() {
        return namesLabel;
    }

    @ViewVisibility
    int getNamedaysVisibility() {
        return namedaysVisibility;
    }

    int getMaxLines() {
        return maxLines;
    }
}
