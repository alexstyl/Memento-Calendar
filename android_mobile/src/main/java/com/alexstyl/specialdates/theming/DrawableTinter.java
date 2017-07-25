package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

public class DrawableTinter {
    private final AttributeExtractor extractor;

    public DrawableTinter(AttributeExtractor extractor) {
        this.extractor = extractor;
    }

    public Drawable createAccentTintedDrawable(@DrawableRes int drawableResId, Context context) {
        return createAccentTintedDrawable(context.getResources().getDrawable(drawableResId), context).mutate();
    }

    public Drawable createAccentTintedDrawable(Drawable drawable, Context context) {
        int accentColor = extractor.extractAccentColorFrom(context);
        drawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}
