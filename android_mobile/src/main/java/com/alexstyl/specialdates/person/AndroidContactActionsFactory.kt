package com.alexstyl.specialdates.person

import android.app.Activity
import android.content.Intent
import android.net.Uri
import java.net.URI

class AndroidContactActionsFactory(val activity: Activity) : ContactActionsFactory {
    override fun dial(phoneNumber: String) = {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        activity.startActivity(intent)
    }

    override fun view(data: URI, mimetype: String) = {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(data.toString()), mimetype)
        activity.startActivity(intent)
    }

    override fun message(phoneNumber: String) = {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("sms:" + phoneNumber)
        activity.startActivity(intent)
    }

    override fun email(emailAdress: String) = {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAdress))
        activity.startActivity(intent)
    }

    override fun view(data: URI) = {
        val intent = Intent(Intent.ACTION_VIEW)
        activity.startActivity(intent)
    }

}

interface ContactActionsFactory {
    fun dial(phoneNumber: String): () -> Unit
    fun view(data: URI, mimetype: String): () -> Unit
    fun view(data: URI): () -> Unit
    fun message(phoneNumber: String): () -> Unit
    fun email(emailAdress: String): () -> Unit
}
