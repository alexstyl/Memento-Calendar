package com.alexstyl.specialdates.widgetprovider;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.alexstyl.specialdates.date.Date;

public class WidgetColorCalculator {

    private static final int modifier = 15;
    private static final double max = 100.00;
    @ColorInt
    private static final int highlightColor = Color.RED;
    @ColorInt
    private final int baseColor;

    public WidgetColorCalculator(int baseColor) {
        this.baseColor = baseColor;
    }

    public int getColor(Date today, Date nextEvent) {

        int dayDiff = today.daysDifferenceTo(nextEvent);
        if (dayDiff <= 3) {
            return getColorForDaysDifference(dayDiff);
        }
        return baseColor;
    }

    public int getColorForDaysDifference(int dayDiff) {
        float modifier = getModifier(dayDiff);
        return blend(baseColor, highlightColor, modifier);
    }

    private float getModifier(int dif) {
        return (float) ((max - (dif + 1) * modifier) / max);
    }

    private static int blend(@ColorInt int background, @ColorInt int foreground, float ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        float iRatio = 1.0f - ratio;

        int aA = (background >> 24 & 0xff);
        int aR = ((background & 0xff0000) >> 16);
        int aG = ((background & 0xff00) >> 8);
        int aB = (background & 0xff);

        int bA = (foreground >> 24 & 0xff);
        int bR = ((foreground & 0xff0000) >> 16);
        int bG = ((foreground & 0xff00) >> 8);
        int bB = (foreground & 0xff);

        int A = (int) ((aA * iRatio) + (bA * ratio));
        int R = (int) ((aR * iRatio) + (bR * ratio));
        int G = (int) ((aG * iRatio) + (bG * ratio));
        int B = (int) ((aB * iRatio) + (bB * ratio));

        return A << 24 | R << 16 | G << 8 | B;
    }

}
