package com.alexstyl.specialdates.theming;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class ViewTinter {
    private final AttributeExtractor extractor;

    public ViewTinter(AttributeExtractor extractor) {
        this.extractor = extractor;
    }

    public void tintToAccentColor(ImageButton closeButton) {
        int accentColor = extractor.extractAccentColorFrom(closeButton.getContext());
        Drawable mutate = closeButton.getDrawable().mutate();
        mutate.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
        closeButton.setImageDrawable(mutate);
    }
}
