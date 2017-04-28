package com.alexstyl.specialdates.date;

public class DateConstants {

    /**
     * A value that represents that no year has been specified.
     */
    /*
     * The specific number (4) was used because it's the first positive year in JodaTime which contains the date 29 of February.
     * We currently not allow the user to use any value for a year less than 1900 anyhow. If year 4 is selected somehow through the device database,
     * then we are not going to treat it as a real year ¯\_(ツ)_/¯
     */
    public static final int NO_YEAR = 4;

    private DateConstants() {
        // hide this
    }

    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

}
