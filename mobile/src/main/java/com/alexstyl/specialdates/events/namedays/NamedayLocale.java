package com.alexstyl.specialdates.events.namedays;

import android.support.annotation.RawRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum NamedayLocale {
    gr(true, R.raw.gr_namedays),
    ro(false, R.raw.ro_namedays),
    ru(false, R.raw.ru_namedays),
    lv(false, R.raw.lv_namedays),
    sk(false, R.raw.sk_namedays),
    cs(false, R.raw.cs_namedays);

    private final boolean soundCompared;
    private final int rawResId;

    NamedayLocale(boolean soundCompared, @RawRes int rawResId) {
        this.soundCompared = soundCompared;
        this.rawResId = rawResId;
    }

    public static NamedayLocale from(String displayLanguage) {
        for (NamedayLocale locale : values()) {
            if (locale.name().equalsIgnoreCase(displayLanguage)) {
                return locale;
            }
        }
        throw new DeveloperError("No NamedayLocale found for [%s]", displayLanguage);
    }

    public int getRawResId() {
        return rawResId;
    }

    public boolean isComparedBySounds() {
        return soundCompared;
    }
}
