package com.alexstyl.specialdates.upcoming.widget.today;

class PercentToValueConverter {

    private static final float STEP = 0.25f;

    float progressToPercent(int progress) {
        return progress * STEP;
    }

    int percentToProgress(float percentage) {
        return (int) (percentage / STEP);
    }
}
