package com.alexstyl.specialdates;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.resources.Strings;

public class ShareAppIntentCreator {

    private static final String MARKET_LINK_SHORT = "http://goo.gl/ZQiAsi";

    private final Context context;
    private final Strings stringResource;

    public ShareAppIntentCreator(Context context, Strings stringResource) {
        this.context = context;
        this.stringResource = stringResource;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, createShareText());
        return intent;
    }

    private String createShareText() {
        String appName = context.getString(R.string.app_name);
        return String.format(stringResource.getString(R.string.share_text), appName, MARKET_LINK_SHORT);
    }
}
