package com.alexstyl.specialdates.util;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class AppUtils {

    public static Intent getEmailIntent(String to, String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", to, null));
        if (!TextUtils.isEmpty(subject)) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(text)) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        return emailIntent;
    }

}
