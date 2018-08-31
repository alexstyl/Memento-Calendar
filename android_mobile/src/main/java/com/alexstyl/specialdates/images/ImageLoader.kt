package com.alexstyl.specialdates.images

import android.graphics.Bitmap
import android.support.annotation.Px
import android.widget.ImageView
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.ImageURL

interface ImageLoader {

    fun load(imagePath: ImageURL): Request

    interface Request {

        fun withSize(@Px width: Int, @Px height: Int): FixedSizeRequest

        fun into(imageView: ImageView)

        fun asCircle(): Request
    }

    interface FixedSizeRequest {
        fun synchronously(): Optional<Bitmap>

        fun into(consumer: ImageLoadedConsumer)
    }

}
