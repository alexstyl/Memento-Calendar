package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.CrashAndErrorTracker
import org.joda.time.format.DateTimeFormat
import java.util.Locale

class DateParser(private val errorTracker: CrashAndErrorTracker) {


    @Throws(DateParseException::class)
    fun parse(rawDate: String?): Date {
        return parse(rawDate, false)
    }

    @Throws(DateParseException::class)
    fun parseWithoutYear(rawDate: String): Date {
        return parse(rawDate, true)
    }

    @Throws(DateParseException::class)
    private fun parse(rawDate: String?, removeYear: Boolean): Date {
        for (locale in LOCALES) {
            for (format in SUPPORTED_FORMATS) {
                val formatter = DateTimeFormat.forPattern(format)
                        .withLocale(locale)
                        .withDefaultYear(Months.NO_YEAR)
                try {
                    val parsedDate = formatter.parseLocalDate(rawDate)
                    val dayOfMonth = parsedDate.dayOfMonth
                    @MonthInt val month = parsedDate.monthOfYear
                    val year = parsedDate.year

                    return if (year == Months.NO_YEAR || removeYear) {
                        Date.on(dayOfMonth, month)
                    } else {
                        Date.on(dayOfMonth, month, year)
                    }

                } catch (e: IllegalArgumentException) {
                    if (isNotAboutInvalidFormat(e)) {
                        errorTracker.track(e)
                    }
                }

            }
        }

        throw DateParseException("Unable to parse $rawDate")
    }

    private fun isNotAboutInvalidFormat(e: IllegalArgumentException): Boolean = e.message?.notContains("Invalid format") ?: true

    companion object {

        private val LOCALES: Array<Locale> = arrayOf(Locale.getDefault(), Locale.US)

        private val SUPPORTED_FORMATS = arrayOf(
                "yyyy-MM-dd",
                "--MM-dd",
                "MMM dd, yyyy",
                "MMM dd yyyy",
                "MMM dd",
                "dd MMM yyyy",
                "dd MMM", // 19 Aug 1990
                "yyyyMMdd", // 20110505
                "dd MMM yyyy", "d MMM yyyy", // >>> 6 Δεκ 1980
                "dd/MM/yyyy", //22/04/23
                "yyyy-MM-dd HH:mm:ss.SSSZ", //ISO 8601
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "dd-MM-yyyy", //25-4-1950
                "dd/MMMM/yyyy", // 13/Gen/1972
                "yyyy-MM-dd'T'HH:mm:ssZ", // 1949-02-14T00:00:00Z
                "yyyyMMdd'T'HHmmssZ")// 20151026T083936Z
    }
}

private fun String.notContains(s: String): Boolean? = !contains(s)
