package com.alexstyl.specialdates.images

import android.graphics.Bitmap
import com.novoda.notils.logger.simple.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URI

class ImageDecoder(private val imageLoader: ImageLoader) {

    fun decodeFrom(imageUri: URI): DecodedImage? {
        val bitmapOptional = imageLoader.load(imageUri).withSize(700, 700).synchronously()
        return if (bitmapOptional.isPresent) {
            decodeFrom(bitmapOptional.get())
        } else {
            null
        }
    }

    private fun decodeFrom(bitmap: Bitmap): DecodedImage? {
        val size = bitmap.width * bitmap.height * SIZE_MULTIPLIER
        val stream = ByteArrayOutputStream(size)
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, FULL_QUALITY, stream)
            stream.flush()
            stream.close()
            val bytes = stream.toByteArray()
            return DecodedImage(bytes)
        } catch (e: IOException) {
            Log.w("Unable to serialize photo: " + e.toString())
            return null
        }

    }

    companion object {
        private const val FULL_QUALITY = 100
        private const val SIZE_MULTIPLIER = 4
    }
}
