package com.alexstyl.resources;

import android.support.annotation.DimenRes;
import android.support.annotation.Px;

public interface DimensionResources {
    @Px
    int getPixelSize(@DimenRes int id);
}
