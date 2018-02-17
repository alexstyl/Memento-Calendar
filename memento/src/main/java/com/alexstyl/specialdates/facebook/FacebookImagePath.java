package com.alexstyl.specialdates.facebook;

import java.net.URI;

public final class FacebookImagePath {

    private static final int SIZE = 700;
    private static final String IMG_URL = "https://graph.facebook.com/%s/picture?width=" + SIZE + "&height=" + SIZE;

    private FacebookImagePath() {
        // hide this
    }

    public static URI forUid(long uid) {
        return URI.create(String.format(IMG_URL, uid));
    }
}
