package com.alexstyl.specialdates.date;

public interface Date {

    int NO_YEAR = -1;

    int getDayOfMonth();

    int getMonth();

    int getYear();

    boolean isBefore(Date date);

    boolean isAfter(Date date);

}
