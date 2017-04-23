package com.alexstyl.specialdates.upcoming.widget.today;

import android.support.annotation.ColorRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum WidgetVariant {
    LIGHT(1, R.color.upcoming_widget_light_textcolor, R.color.upcoming_widget_light_background),
    DARK(0, R.color.upcoming_widget_dark_textcolor, R.color.upcoming_widget_dark_background);

    private final int id;
    @ColorRes
    private final int textColor;
    @ColorRes
    private int backgroundColorResId;

    public static WidgetVariant fromId(int variantId) {
        for (WidgetVariant widgetVariant : values()) {
            if (widgetVariant.id == variantId) {
                return widgetVariant;
            }
        }
        throw new DeveloperError("No Widget Variant with id [%d]", variantId);
    }

    WidgetVariant(int id, @ColorRes int textColor, @ColorRes int backgroundColorResId) {
        this.id = id;
        this.textColor = textColor;
        this.backgroundColorResId = backgroundColorResId;
    }

    public int getId() {
        return id;
    }

    @ColorRes
    public int getTextColor() {
        return textColor;
    }

    @ColorRes
    public int getBackgroundColorResId() {
        return backgroundColorResId;
    }
}
