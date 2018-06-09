package com.alexstyl.specialdates.addevent;

import android.graphics.drawable.Drawable;

public class AccountData {

    static final AccountData NO_ACCOUNT = new AccountData(null, null, null);

    private final String name;
    private final String type;
    private final Drawable icon;

    AccountData(String name, String type, Drawable icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    String getAccountName() {
        return name;
    }

    String getAccountType() {
        return type;
    }

    Drawable getIcon() {
        return icon;
    }
}
