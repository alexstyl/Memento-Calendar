package com.alexstyl.specialdates.upcoming;

import android.app.Activity;

import java.util.Locale;

class MonthTitleSetter {

    private final Activity activity;
    private final MonthLabels monthLabels;


    public static MonthTitleSetter createSetterFor(Activity activity) {
        MonthLabels monthLabels = MonthLabels.forLocale(Locale.getDefault());

        return new MonthTitleSetter(activity, monthLabels);
    }

    MonthTitleSetter(Activity activity, MonthLabels monthLabels) {
        this.activity = activity;
        this.monthLabels = monthLabels;
    }


    void updateWithMonth(int monthToDisplay) {
        String monthLabel = monthLabels.getMonthOfYear(monthToDisplay);
        activity.setTitle(monthLabel);
    }

}
