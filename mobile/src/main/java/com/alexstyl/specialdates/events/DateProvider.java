package com.alexstyl.specialdates.events;

public class DateProvider {

    private static final String SEPARATOR = "-";

    public DayDate from(String text) {
        int dayToMonth = text.lastIndexOf(SEPARATOR);
        int monthToYear = text.lastIndexOf(SEPARATOR, dayToMonth - 1);

        int day = Integer.valueOf(text.substring(dayToMonth + 1, text.length()));
        int month = Integer.valueOf(text.substring(monthToYear + 1, dayToMonth));
        // need to chek if we have year

        if (text.startsWith(SEPARATOR)) {
            return DayDate.newInstance(day, month);
        }

        int yearToMonth = text.indexOf(SEPARATOR);
        int year = Integer.valueOf(text.substring(0, yearToMonth));
        return DayDate.newInstance(day, month, year);
    }


}
