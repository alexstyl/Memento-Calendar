package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateConstants;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.MonthInt;

import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum DateParser {
    INSTANCE;

    private static Locale[] LOCALES;

    static {
        LOCALES = new Locale[]{Locale.getDefault(), Locale.US};
    }

    private final static String[] DATE_FORMATS = {
            "yyyy-MM-dd", "--MM-dd",
            "MMM dd, yyyy", "MMM dd yyyy", "MMM dd",

            "dd MMM yyyy", "dd MMM",// 19 Aug 1990
            "yyyyMMdd", // 20110505
            "dd MMM yyyy",
            "d MMM yyyy", // >>> 6 Δεκ 1980
            "dd/MM/yyyy", //22/04/23
            "yyyy-MM-dd HH:mm:ss.SSSZ", //ISO 8601
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "dd-MM-yyyy", //25-4-1950
            "dd/MMMM/yyyy", // 13/Gen/1972
            "yyyy-MM-dd'T'HH:mm:ssZ", // 1949-02-14T00:00:00Z
            "yyyyMMdd'T'HHmmssZ", // 20151026T083936Z

    };

    public Date parse(String rawDate) throws DateParseException {
        return parse(rawDate, false);
    }

    public Date parseWithoutYear(String rawDate) throws DateParseException {
        return parse(rawDate, true);
    }

    private Date parse(String rawDate, boolean removeYear) throws DateParseException {
        for (Locale locale : LOCALES) {
            for (String format : DATE_FORMATS) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format)
                        .withLocale(locale)
                        .withDefaultYear(DateConstants.NO_YEAR);
                try {
                    LocalDate parsedDate = formatter.parseLocalDate(rawDate);
                    int dayOfMonth = parsedDate.getDayOfMonth();
                    @MonthInt int month = parsedDate.getMonthOfYear();
                    int year = parsedDate.getYear();

                    if (year == DateConstants.NO_YEAR || removeYear) {
                        return Date.on(dayOfMonth, month);
                    } else {
                        return Date.on(dayOfMonth, month, year);
                    }

                } catch (IllegalArgumentException e) {
                    if (BuildConfig.DEBUG && isNotAboutInvalidFormat(e)) {
                        e.printStackTrace();
                    }
                }
            }
        }

        throw new DateParseException("Unable to parse " + rawDate);
    }

    private boolean isNotAboutInvalidFormat(IllegalArgumentException e) {
        return !e.getMessage().contains("Invalid format");
    }
}
