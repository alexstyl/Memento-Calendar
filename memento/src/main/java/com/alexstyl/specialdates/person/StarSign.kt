package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.APRIL
import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.Months.FEBRUARY
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.JULY
import com.alexstyl.specialdates.date.Months.JUNE
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.MAY
import com.alexstyl.specialdates.date.Months.NOVEMBER
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.date.Months.SEPTEMBER
import com.alexstyl.specialdates.date.dateOn

enum class StarSign(val emoji: String, val from: Date, val to: Date) {
    AQUARIUS("\u2652", dateOn(20, JANUARY), dateOn(18, FEBRUARY)),
    PISCES("\u2653", dateOn(19, FEBRUARY), dateOn(20, MARCH)),
    ARIES("\u2648", dateOn(21, MARCH), dateOn(19, APRIL)),
    TAURUS("\u2649", dateOn(20, APRIL), dateOn(20, MAY)),
    GEMINI("\u264a", dateOn(21, MAY), dateOn(20, JUNE)),
    CANCER("\u264b", dateOn(21, JUNE), dateOn(22, JULY)),
    LEO("\u264c", dateOn(23, JULY), dateOn(22, AUGUST)),
    VIRGO("\u264d", dateOn(23, AUGUST), dateOn(22, SEPTEMBER)),
    LIBRA("\u264e", dateOn(23, SEPTEMBER), dateOn(22, OCTOBER)),
    SCORPIO("\u264f", dateOn(23, OCTOBER), dateOn(21, NOVEMBER)),
    SAGITTARIUS("\u2650", dateOn(22, NOVEMBER), dateOn(21, DECEMBER)),
    CAPRICORN("\u2651", dateOn(22, DECEMBER), dateOn(19, JANUARY));

    companion object {
        fun forDateOfBirth(birthday: Date): StarSign {
            enumValues<StarSign>().forEach {
                if (birthday.startingDateOf(it) || birthday.endingDateOf(it)) {
                    return it
                }
            }
            throw IllegalStateException("Couldn't find starSign for $birthday")
        }
    }
}

private fun Date.startingDateOf(startSign: StarSign) = this.month == startSign.from.month && this.dayOfMonth >= startSign.from.dayOfMonth

private fun Date.endingDateOf(startSign: StarSign) = this.month == startSign.to.month && this.dayOfMonth <= startSign.to.dayOfMonth
