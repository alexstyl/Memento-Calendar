package com.alexstyl.specialdates.ui;

import android.support.annotation.DrawableRes;

import com.alexstyl.specialdates.R;

import java.util.HashMap;
import java.util.Map;

public enum Section {

    CONTACTS(0, R.drawable.ic_contacts, R.drawable.ic_contacts_disabled),
    BANKHOLIDAYS(1, R.drawable.ic_bankholidays, R.drawable.ic_bankholidays_disabled),
    NAMEDAYS(2, R.drawable.ic_namedays, R.drawable.ic_namedays_disabled);

    private final int id;
    @DrawableRes
    private final int enabledResId;
    @DrawableRes
    private final int disabledResId;

    Section(int id, @DrawableRes int enabledResId, @DrawableRes int disabledResId) {
        this.id = id;
        this.enabledResId = enabledResId;
        this.disabledResId = disabledResId;
    }

    private static final Map<Integer, Section> mapping;

    static {
        mapping = new HashMap<>(values().length);
        for (Section section : values()) {
            mapping.put(section.id, section);
        }
    }

    public static Section ofId(int position) {
        return mapping.get(position);
    }

    @DrawableRes
    public int getEnabledResId() {
        return enabledResId;
    }

    @DrawableRes
    public int getDisabledResId() {
        return disabledResId;
    }
}
