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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NamedaysViewModel that = (NamedaysViewModel) o;

        if (namedaysVisibility != that.namedaysVisibility) {
            return false;
        }
        if (maxLines != that.maxLines) {
            return false;
        }
        return namesLabel.equals(that.namesLabel);

    }

    @Override
    public int hashCode() {
        int result = namesLabel.hashCode();
        result = 31 * result + namedaysVisibility;
        result = 31 * result + maxLines;
        return result;
    }
}
