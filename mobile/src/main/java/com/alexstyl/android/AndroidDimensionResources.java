package com.alexstyl.android;

import android.content.res.Resources;
import android.support.annotation.DimenRes;

import com.alexstyl.resources.DimensionResources;

public final class AndroidDimensionResources implements DimensionResources {

    private final Resources resources;

    public AndroidDimensionResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public int getPixelSize(@DimenRes int id) {
        return resources.getDimensionPixelSize(id);
    }

}
