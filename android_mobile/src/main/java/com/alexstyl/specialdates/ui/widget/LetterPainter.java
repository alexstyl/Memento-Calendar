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
    private static final int[] mVariants = {
            R.color.avatar_variant_1,
            R.color.avatar_variant_2,
            R.color.avatar_variant_3,
            R.color.avatar_variant_4,
            R.color.avatar_variant_5,
    };

    private static final int VARIANT_COUNT;

    static {
        VARIANT_COUNT = mVariants.length;
    }

    @ColorInt
    static int getVariant(Resources res, int i) {
        if (i < 0) {
            i = i * (-1);
        }
        if (i >= VARIANT_COUNT) {
            i = (i % VARIANT_COUNT);
            if (i >= VARIANT_COUNT) {
                i = i / VARIANT_COUNT;
            }
        }
        return res.getColor(mVariants[i]);
    }

}

