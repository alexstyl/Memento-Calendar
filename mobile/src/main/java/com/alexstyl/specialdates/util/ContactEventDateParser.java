package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.ParsedDate;

import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ContactEventDateParser {

    private static Locale[] LOCALES;

    private final static String[] dateFormats = {
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

    /**
     * Parses the given date using the default date formats
     */
    public ParsedDate parse(String date) throws DateParseException {
        return parse(date, dateFormats);
    }

    /**
     * Parses the given date, using the given date formats
     */
    public ParsedDate parse(String date, String[] formats) throws DateParseException {
        if (LOCALES == null) {
            LOCALES = new Locale[]{Locale.getDefault(), Locale.US};
        }

        if (date != null) {
            for (Locale locale : LOCALES) {
                for (String format : formats) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(format)
                            .withLocale(locale);
                    try {
                        LocalDate dt = formatter.parseLocalDate(date);
                        int day = dt.getDayOfMonth();
                        int month = dt.getMonthOfYear();
                        int year = dt.getYear();

                        if (year == -1) {
                            return new ParsedDate(day, month);
                        } else {
                            return new ParsedDate(day, month, year);
                        }

                    } catch (IllegalArgumentException e) {
                        // do not report failed parsing attempts
                    }
                }
            }
        }

        throw new DateParseException("Unable to parse " + date);
    }
}
