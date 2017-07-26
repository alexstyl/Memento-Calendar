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


    public Drawable tintWithPrimaryColor(@DrawableRes int drawableResId, Context context) {
        Drawable mutateableDrawable = context.getResources().getDrawable(drawableResId).mutate();
        int accentColor = extractor.extractPrimaryColorFrom(context);
        mutateableDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
        return mutateableDrawable;
    }

    public Drawable tintWithAccentColor(Drawable drawable, Context context) {
        int accentColor = extractor.extractAccentColorFrom(context);
        drawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}
