package com.alexstyl.specialdates.date;

public interface Date {

    int JANUARY = 1;
    int FEBRUARY = 2;
    int MARCH = 3;
    int APRIL = 4;
    int MAY = 5;
    int JUNE = 6;
    int JULY = 7;
    int AUGUST = 8;
    int SEPTEMBER = 9;
    int OCTOBER = 10;
    int NOVEMBER = 11;
    int DECEMBER = 12;

    int MONDAY = 1;
    int TUESDAY = 2;
    int WEDNESDAY = 3;
    int THURSDAY = 4;
    int FRIDAY = 5;
    int SATURDAY = 6;
    int SUNDAY = 7;

    int getDayOfMonth();

    @MonthInt
    int getMonth();

}
