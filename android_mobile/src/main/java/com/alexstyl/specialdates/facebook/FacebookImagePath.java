package com.alexstyl.specialdates.facebook;

import java.net.URI;

public final class FacebookImagePath {
    private static final String IMG_URL = "https://graph.facebook.com/%s/picture?width=250&height=250";

    public static URI forUid(long uid) {
        return URI.create(String.format(IMG_URL, uid));
    }
}
