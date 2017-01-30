package com.alexstyl.specialdates.date;

import java.util.Comparator;

public enum DateComparator implements Comparator<Date> {
    INSTANCE;

    @Override
    public int compare(Date left, Date right) {
        if (left.hasYear() && right.hasYear()) {
            int yearOne = left.getYear();
            int yearTwo = right.getYear();
            if (yearOne > yearTwo) {
                return 1;
            } else if (yearOne < yearTwo) {
                return -1;
            }
        }
        if (left.getMonth() < right.getMonth()) {
            return -1;
        } else if (left.getMonth() > right.getMonth()) {
            return 1;
        }
        return left.getDayOfMonth() - right.getDayOfMonth();
    }

}
