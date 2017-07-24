package com.alexstyl.specialdates.person

import android.app.Activity
import android.content.Intent
import android.net.Uri
import java.net.URI

class AndroidContactActionsFactory(val activity: Activity) : ContactActionsFactory {

    override fun dialNumber(phoneNumber: String) = {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        activity.startActivity(intent)
    }

    override fun view(data: URI) = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.toString()))
        activity.startActivity(intent)
    }

}

interface ContactActionsFactory {

    fun dialNumber(phoneNumber: String): () -> Unit
    fun view(data: URI): () -> Unit
}
