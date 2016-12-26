package com.alexstyl.resources;

import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

public interface StringResources {
    String getString(@StringRes int id);

    String getString(@StringRes int id, Object... formatArgs);

    String getQuantityString(@PluralsRes int id, int size);
}
