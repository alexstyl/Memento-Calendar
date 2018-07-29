package com.alexstyl.specialdates.facebook

import com.alexstyl.specialdates.contact.ImageURL

object FacebookImagePath {

    private const val SIZE = 700
    private const val IMG_URL = "https://graph.facebook.com/%s/picture?width=$SIZE&height=$SIZE"

    fun forUid(uid: Long): ImageURL {
        return String.format(IMG_URL, uid)
    }
}
