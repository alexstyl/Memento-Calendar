package com.alexstyl.specialdates;

import android.content.Intent;

import com.alexstyl.resources.Strings;

public class ShareAppIntentCreator {

    private static final String MARKET_LINK_SHORT = "http://goo.gl/ZQiAsi";

    private final Strings strings;

    public ShareAppIntentCreator(Strings strings) {
        this.strings = strings;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, createShareText());
        return intent;
    }

    private String createShareText() {
        return String.format(strings.shareText(), strings.appName(), MARKET_LINK_SHORT);
    }
}
