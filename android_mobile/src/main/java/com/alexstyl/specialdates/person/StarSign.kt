package com.alexstyl.specialdates.person

import android.support.annotation.StringRes
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Date.on
import com.alexstyl.specialdates.date.DateConstants.*

enum class StarSign(@StringRes val labelStringRes: Int, val from: Date, val to: Date) {
    AQUARIUS(R.string.starsigns_aquarius, on(20, JANUARY), on(18, FEBRUARY)),
    PISCES(R.string.starsigns_pisces, on(19, FEBRUARY), on(20, MARCH)),
    ARIES(R.string.starsigns_aries, on(21, MARCH), on(19, APRIL)),
    TAURUS(R.string.starsigns_taurus, on(20, APRIL), on(20, MAY)),
    GEMINI(R.string.starsigns_gemini, on(21, MAY), on(20, JUNE)),
    CANCER(R.string.starsigns_cancer, on(21, JUNE), on(22, JULY)),
    LEO(R.string.starsigns_leo, on(23, JULY), on(22, AUGUST)),
    VIRGO(R.string.starsigns_virgo, on(23, AUGUST), on(22, SEPTEMBER)),
    LIBRA(R.string.starsigns_libra, on(23, SEPTEMBER), on(22, OCTOBER)),
    SCORPIO(R.string.starsigns_scorpio, on(23, OCTOBER), on(21, NOVEMBER)),
    SAGITTARIUS(R.string.starsigns_sagittarius, on(22, NOVEMBER), on(21, DECEMBER)),
    CAPRICORN(R.string.starsigns_capricorn, on(22, DECEMBER), on(19, JANUARY));

    companion object {
        fun forDateOfBirth(birthday: Date): StarSign {
            enumValues<StarSign>().forEach {
                if (birthday.month == it.from.month && birthday.dayOfMonth >= it.from.dayOfMonth
                        || birthday.month == it.to.month && birthday.dayOfMonth <= it.to.dayOfMonth) {
                    return it
                }
            }

            throw IllegalStateException("Couldn't find starSign for " + birthday)
        }
    }

}
