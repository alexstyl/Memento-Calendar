package com.alexstyl.specialdates.images

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import java.io.InputStream

class AndroidContactsImageDownloader(context: Context) : BaseImageDownloader(context) {

    override fun getStreamFromContent(imageUri: String?, extra: Any?): InputStream {
        val res = context.contentResolver
        val uri = Uri.parse(imageUri)

        return if (imageUri != null && imageUri.startsWith("content://com.android.contacts/")) {
            ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true)
        } else res.openInputStream(uri)
    }
}
