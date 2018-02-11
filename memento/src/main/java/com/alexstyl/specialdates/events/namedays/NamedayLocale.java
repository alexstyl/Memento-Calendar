package com.alexstyl.specialdates.events.namedays;

public enum NamedayLocale {
    GREEK("gr", true),
    ROMANIAN("ro", false),
    RUSSIAN("ru", false),
    LATVIAN("lv", false),
    LATVIAN_EXTENDED("lv_ext", false),
    SLOVAK("sk", false),
    ITALIAN("it", false),
    CZECH("cs", false),
    HUNGARIAN("hu", false);

    private final String shortCode;
    private final boolean soundCompared;

    NamedayLocale(String shortCode, boolean soundCompared) {
        this.shortCode = shortCode;
        this.soundCompared = soundCompared;
    }

    public static NamedayLocale from(String displayLanguage) {
        for (NamedayLocale locale : values()) {
            if (locale.getCountryCode().equalsIgnoreCase(displayLanguage)) {
                return locale;
            }
        }
        throw new IllegalArgumentException("No NamedayLocale found for "+ displayLanguage);
    }

    public boolean isComparedBySound() {
        return soundCompared;
    }

    public String getCountryCode() {
        return shortCode;
    }
}
