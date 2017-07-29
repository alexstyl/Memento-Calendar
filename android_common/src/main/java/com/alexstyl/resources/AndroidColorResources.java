package com.alexstyl.resources;

import android.content.res.Resources;
import android.support.annotation.ColorRes;

final class AndroidColorResources implements ColorResources {

    private final Resources resources;

    AndroidColorResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public int getColor(@ColorRes int colorRes) {
        return resources.getColor(colorRes);
    }
}
