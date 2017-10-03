package com.alexstyl.specialdates.upcoming.widget.today;

final class UserOptions {
    private final float opacityLevel;
    private final WidgetVariant widgetVariant;

    UserOptions(float opacityLevel, WidgetVariant widgetVariant) {
        this.opacityLevel = opacityLevel;
        this.widgetVariant = widgetVariant;
    }

    float getOpacityLevel() {
        return opacityLevel;
    }

    WidgetVariant getWidgetVariant() {
        return widgetVariant;
    }
}
