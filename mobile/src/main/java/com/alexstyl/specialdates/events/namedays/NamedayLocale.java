package com.alexstyl.specialdates.events.namedays;

import android.support.annotation.RawRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum NamedayLocale {
    GREEK("gr", true, R.raw.gr_namedays),
    ROMANIAN("ro", false, R.raw.ro_namedays),
    RUSSIAN("ru", false, R.raw.ru_namedays),
    LATVIAN("lv", false, R.raw.lv_namedays),
    SLOVAK("sk", false, R.raw.sk_namedays),
    CZECH("cs", false, R.raw.cs_namedays);

    private final String shortCode;
    private final boolean soundCompared;
    private final int rawResId;

    NamedayLocale(String shortCode, boolean soundCompared, @RawRes int rawResId) {
        this.shortCode = shortCode;
        this.soundCompared = soundCompared;
        this.rawResId = rawResId;
    }

    public static NamedayLocale from(String displayLanguage) {
        for (NamedayLocale locale : values()) {
            if (locale.getShortCode().equalsIgnoreCase(displayLanguage)) {
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

    public String getShortCode() {
        return shortCode;
    }
}
