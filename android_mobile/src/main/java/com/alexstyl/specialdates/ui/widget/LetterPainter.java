package com.alexstyl.specialdates.ui.widget;

import android.content.res.Resources;
import android.support.annotation.ColorInt;

import com.alexstyl.specialdates.R;

final class LetterPainter {

    private LetterPainter() {
        // hide this
    }

    /**
     * The different color variants to draw
     */
    private static final int[] BACKGROUND_VARIANTS = {
            R.color.avatar_variant_1,
            R.color.avatar_variant_2,
            R.color.avatar_variant_3,
            R.color.avatar_variant_4,
            R.color.avatar_variant_5,
    };

    private static final int VARIANT_COUNT;

    static {
        VARIANT_COUNT = BACKGROUND_VARIANTS.length;
    }

    @ColorInt
    static int getVariant(Resources res, int i2) {
        int variant = Math.abs(i2);
        variant = normalise(variant);
        return res.getColor(BACKGROUND_VARIANTS[variant]);
    }

    private static int normalise(int variant) {
        if (variant >= VARIANT_COUNT) {
            variant = (variant % VARIANT_COUNT);
            if (variant >= VARIANT_COUNT) {
                variant = variant / VARIANT_COUNT;
            }
        }
        return variant;
    }

}

