package com.alexstyl.android;

import android.content.res.Resources;
import android.support.annotation.ColorRes;

import com.alexstyl.resources.ColorResources;

public final class AndroidColorResources implements ColorResources {

    private final Resources resources;

    public AndroidColorResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public int getColor(@ColorRes int colorRes) {
        return resources.getColor(colorRes);
    }
}
