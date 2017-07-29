package com.alexstyl.resources;

import android.content.res.Resources;
import android.support.annotation.DimenRes;

final class AndroidDimensionResources implements DimensionResources {

    private final Resources resources;

    AndroidDimensionResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public int getPixelSize(@DimenRes int id) {
        return resources.getDimensionPixelSize(id);
    }

}
