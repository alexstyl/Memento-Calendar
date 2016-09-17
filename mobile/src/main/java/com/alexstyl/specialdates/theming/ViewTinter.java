package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class ViewTinter {
    private final AttributeExtractor extractor;

    public ViewTinter(AttributeExtractor extractor) {
        this.extractor = extractor;
    }

    public Drawable createAccentTintedDrawable(Drawable mutate, Context context) {
        int accentColor = extractor.extractAccentColorFrom(context);
        mutate.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
        return mutate;
    }
}
