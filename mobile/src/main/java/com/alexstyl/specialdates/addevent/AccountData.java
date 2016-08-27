package com.alexstyl.specialdates.addevent;

import android.graphics.drawable.Drawable;

public class AccountData {

    public static final AccountData NO_ACCOUNT = new AccountData(null, null, null);
    private final String name;
    private final String type;

    private final Drawable icon;

    public AccountData(String name, String type, Drawable icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public String getAccountName() {
        return name;
    }

    public String getAccountType() {
        return type;
    }

    public Drawable getIcon() {
        return icon;
    }
}
