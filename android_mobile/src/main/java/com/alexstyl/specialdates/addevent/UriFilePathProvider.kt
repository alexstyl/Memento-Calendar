package com.alexstyl.specialdates.addevent

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.alexstyl.specialdates.BuildConfig

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class UriFilePathProvider(private val context: Context) {

    fun uriFor(file: File): Uri {
        return FileProvider.getUriForFile(
                context,
                BuildConfig.FILE_PROVIDER,
                file
        )
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        return image
    }

}
