package com.alexstyl.specialdates.facebook

import java.net.URI

object FacebookImagePath {

    private const val SIZE = 700
    private const val IMG_URL = "https://graph.facebook.com/%s/picture?width=$SIZE&height=$SIZE"

    fun forUid(uid: Long): URI {
        return URI.create(String.format(IMG_URL, uid))
    }
}
