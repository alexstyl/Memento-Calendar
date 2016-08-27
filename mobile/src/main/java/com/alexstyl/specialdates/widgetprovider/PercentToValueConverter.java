package com.alexstyl.specialdates.widgetprovider;

public class PercentToValueConverter {

    private static final float STEP = 0.25f;

    public float progressToPercent(int progress) {
        return progress * STEP;
    }

    public int percentToProgress(float percentage) {
        return (int) (percentage / STEP);
    }
}
