package com.alexstyl.specialdates.search;

import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;

import java.util.Locale;

final class DumbTestResources implements StringResources {
    @Override
    public String getString(@StringRes int id) {
        switch (id) {
            case R.string.birthday:
                return "Birthday";
            case R.string.nameday:
                return "Nameday";
            case R.string.Anniversary:
                return "Anniversary";
            case R.string.Other:
                return "Other";
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString(@StringRes int id, Object... formatArgs) {
        switch (id) {
            case R.string.turns_age:
                return String.format(Locale.US, "Turns %1$d", formatArgs);
            case R.string.search_event_label:
                return String.format("%1$s on %2$s", formatArgs);
            default:
                throw new UnsupportedOperationException("Unsupported id: " + id);
        }
    }

    @Override
    public String getQuantityString(@PluralsRes int id, int size) {
        throw new UnsupportedOperationException();
    }
}
