package com.alexstyl.specialdates.facebook;

import android.net.Uri;

enum ImagePathCreator {
    INSTANCE;
    private static final String IMG_URL = "https://graph.facebook.com/%s/picture?width=250&height=250";

    public Uri forUid(long uid) {
        return Uri.parse(String.format(IMG_URL, uid));
    }
}
