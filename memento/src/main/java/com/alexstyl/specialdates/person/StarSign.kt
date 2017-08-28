package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Date.Companion.on
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

enum class StarSign(val emoji: String, val from: Date, val to: Date) {
    AQUARIUS("\u2652", on(20, JANUARY), on(18, FEBRUARY)),
    PISCES("\u2653", on(19, FEBRUARY), on(20, MARCH)),
    ARIES("\u2648", on(21, MARCH), on(19, APRIL)),
    TAURUS("\u2649", on(20, APRIL), on(20, MAY)),
    GEMINI("\u264a", on(21, MAY), on(20, JUNE)),
    CANCER("\u264b", on(21, JUNE), on(22, JULY)),
    LEO("\u264c", on(23, JULY), on(22, AUGUST)),
    VIRGO("\u264d", on(23, AUGUST), on(22, SEPTEMBER)),
    LIBRA("\u264e", on(23, SEPTEMBER), on(22, OCTOBER)),
    SCORPIO("\u264f", on(23, OCTOBER), on(21, NOVEMBER)),
    SAGITTARIUS("\u2650", on(22, NOVEMBER), on(21, DECEMBER)),
    CAPRICORN("\u2651", on(22, DECEMBER), on(19, JANUARY));

    companion object {
        fun forDateOfBirth(birthday: Date): StarSign {
            enumValues<StarSign>().forEach {
                if (birthday.startingDateOf(it) || birthday.endingDateOf(it)) {
                    return it
                }
            }
            throw IllegalStateException("Couldn't find starSign for " + birthday)
        }
    }
}

private fun Date.startingDateOf(startSign: StarSign) = this.month == startSign.from.month && this.dayOfMonth >= startSign.from.dayOfMonth

private fun Date.endingDateOf(startSign: StarSign) = this.month == startSign.to.month && this.dayOfMonth <= startSign.to.dayOfMonth
