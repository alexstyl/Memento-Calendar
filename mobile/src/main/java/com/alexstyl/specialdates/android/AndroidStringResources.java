package com.alexstyl.specialdates.android;

import android.content.res.Resources;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import com.alexstyl.StringResources;

/**
 * Wrapper class of Android's {@link Resources} string related methods
 */
public final class AndroidStringResources implements StringResources {

    private final Resources resources;

    public AndroidStringResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getString(@StringRes int id) {
        return resources.getString(id);
    }

    @Override
    public String getString(@StringRes int id, Object... formatArgs) {
        return resources.getString(id, formatArgs);
    }

    @Override
    public String getQuantityString(@PluralsRes int id, int size) {
        return resources.getQuantityString(id, size);
    }
}
