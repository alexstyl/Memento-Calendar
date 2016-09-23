package com.alexstyl.specialdates.date;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A 1-indexed integer that represents a Month
 */
@Retention(RetentionPolicy.CLASS)
@IntDef({
        Date.JANUARY,
        Date.FEBRUARY,
        Date.MARCH,
        Date.APRIL,
        Date.MAY,
        Date.JUNE,
        Date.JULY,
        Date.AUGUST,
        Date.SEPTEMBER,
        Date.OCTOBER,
        Date.NOVEMBER,
        Date.DECEMBER,
})
public @interface MonthInt {
}
