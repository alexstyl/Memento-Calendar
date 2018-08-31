package com.alexstyl.specialdates.events.namedays

enum class NamedayLocale(val countryCode: String) {
    GREEK("gr"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    LATVIAN("lv"),
    LATVIAN_EXTENDED("lv_ext"),
    SLOVAK("sk"),
    ITALIAN("it"),
    CZECH("cs"),
    HUNGARIAN("hu");


    companion object {

        fun from(displayLanguage: String): NamedayLocale {
            for (locale in values()) {
                if (locale.countryCode.equals(displayLanguage, ignoreCase = true)) {
                    return locale
                }
            }
            throw IllegalArgumentException("No NamedayLocale found for $displayLanguage")
        }
    }
}
