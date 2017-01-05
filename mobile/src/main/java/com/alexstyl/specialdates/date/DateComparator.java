package com.alexstyl.specialdates.date;

import java.util.Comparator;

public enum DateComparator implements Comparator<Date> {
    INSTANCE;

    @Override
    public int compare(Date o1, Date o2) {
        if (o1.hasYear() && o2.hasYear()) {
            int yearOne = o1.getYear();
            int yearTwo = o2.getYear();
            if (yearOne > yearTwo) {
                return 1;
            } else if (yearOne < yearTwo) {
                return -1;
            }
        }
        if (o1.getMonth() < o2.getMonth()) {
            return -1;
        } else if (o1.getMonth() > o2.getMonth()) {
            return 1;
        }
        return o1.getDayOfMonth() - o2.getDayOfMonth();
    }

}
