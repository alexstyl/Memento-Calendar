package com.alexstyl.specialdates.events.namedays;

import android.support.annotation.RawRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum NamedayLocale {
    GREEK("gr", true, R.string.Greek, R.raw.gr_namedays),
    ROMANIAN("ro", false, R.string.Romanian, R.raw.ro_namedays),
    RUSSIAN("ru", false, R.string.Russian, R.raw.ru_namedays),
    LATVIAN("lv", false, R.string.Latvian_Traditional, R.raw.lv_namedays),
    LATVIAN_EXTENDED("lv_LV", false, R.string.Latvian_Extended, R.raw.lv_ext_namedays),
    SLOVAK("sk", false, R.string.Slovak, R.raw.sk_namedays),
    ITALIAN("it", false, R.string.Italian, R.raw.it_namedays),
    CZECH("cs", false, R.string.Czech, R.raw.cs_namedays);

    private final String shortCode;
    private final boolean soundCompared;
    @StringRes
    private int languageName;
    @RawRes
    private final int rawResId;

    NamedayLocale(String shortCode, boolean soundCompared, @StringRes int languageName, @RawRes int rawResId) {
        this.shortCode = shortCode;
        this.soundCompared = soundCompared;
        this.languageName = languageName;
        this.rawResId = rawResId;
    }

    public static NamedayLocale from(String displayLanguage) {
        for (NamedayLocale locale : values()) {
            if (locale.getCountryCode().equalsIgnoreCase(displayLanguage)) {
                return locale;
            }
        }
        throw new DeveloperError("No NamedayLocale found for [%s]", displayLanguage);
    }

    public int getRawResId() {
        return rawResId;
    }

    public boolean isComparedBySound() {
        return soundCompared;
    }

    public String getCountryCode() {
        return shortCode;
    }

    @StringRes
    public int getLanguageNameResId() {
        return languageName;
    }
}
