package com.alexstyl.specialdates.upcoming.widget.today;

import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;

public class TransparencyColorCalculator {

    @ColorInt
    public int calculateColor(@ColorInt int color, float opacity) {
        return ColorUtils.setAlphaComponent(color, percentToValue(opacity));
    }

    private static int percentToValue(float opacity) {
        return (int) (opacity * 255);
    }
}
