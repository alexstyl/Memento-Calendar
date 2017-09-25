package com.alexstyl.specialdates.ui.widget;

import android.content.res.Resources;
import android.support.annotation.ColorInt;

import com.alexstyl.specialdates.R;

public class LetterPainter {

    private final Resources resources;

    LetterPainter(Resources resources) {
        this.resources = resources;
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

    private static final int VARIANT_COUNT = BACKGROUND_VARIANTS.length;

    @ColorInt
    public int getVariant(int i2) {
        int variant = Math.abs(i2);
        variant = normalise(variant);
        return resources.getColor(BACKGROUND_VARIANTS[variant]);
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

