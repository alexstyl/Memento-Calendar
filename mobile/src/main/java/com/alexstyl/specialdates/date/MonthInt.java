package com.alexstyl.specialdates.date;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A 1-indexed integer that represents a Month
 */
@Retention(RetentionPolicy.CLASS)
@IntDef({
        DateConstants.JANUARY,
        DateConstants.FEBRUARY,
        DateConstants.MARCH,
        DateConstants.APRIL,
        DateConstants.MAY,
        DateConstants.JUNE,
        DateConstants.JULY,
        DateConstants.AUGUST,
        DateConstants.SEPTEMBER,
        DateConstants.OCTOBER,
        DateConstants.NOVEMBER,
        DateConstants.DECEMBER,
})
public @interface MonthInt {
}
