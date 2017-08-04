package com.alexstyl.specialdates.date;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A 1-indexed integer that represents a Month
 */
@Retention(RetentionPolicy.CLASS)
@IntDef({
        Months.JANUARY,
        Months.FEBRUARY,
        Months.MARCH,
        Months.APRIL,
        Months.MAY,
        Months.JUNE,
        Months.JULY,
        Months.AUGUST,
        Months.SEPTEMBER,
        Months.OCTOBER,
        Months.NOVEMBER,
        Months.DECEMBER,
})
public @interface MonthInt {
}
