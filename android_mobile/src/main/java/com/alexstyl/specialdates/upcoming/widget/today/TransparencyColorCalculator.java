package com.alexstyl.specialdates.upcoming.widget.today;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.graphics.ColorUtils;

final class TransparencyColorCalculator {

    private static final int FULL_ALPHA = 255;

    @ColorInt
    int calculateColor(@ColorInt int color, @FloatRange(from = 0.0f, to = 1.0f) float opacityPercentage) {
        return ColorUtils.setAlphaComponent(color, percentToValue(opacityPercentage));
    }

    private static int percentToValue(float opacity) {
        return (int) (opacity * FULL_ALPHA);
    }
}
