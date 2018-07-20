package com.alexstyl.specialdates.person

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.net.Uri
import com.alexstyl.android.toUri
import com.alexstyl.specialdates.SimpleChromeCustomTabsObserver
import com.alexstyl.specialdates.home.HomeNavigator
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.ui.base.MementoActivity
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs
import java.net.URI

class AndroidContactActions(private val activity: MementoActivity,
                            private val attributeExtractor: AttributeExtractor) : ContactActions {

    init {
        activity.lifecycle.addObserver(SimpleChromeCustomTabsObserver(activity))
    }

    override fun dial(phoneNumber: String) = {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        activity.startActivity(intent)
    }

    override fun message(phoneNumber: String) = {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("sms:$phoneNumber")
        activity.startActivity(intent)
    }

    override fun email(emailAdress: String) = {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAdress))
        activity.startActivity(intent)
    }

    override fun view(data: URI, mimetype: String) = {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(data.toString()), mimetype)
        activity.startActivity(intent)
    }

    override fun view(data: URI) = {
        if (data.isWebPage()) {
            viewWithCustomTabs(data)
        } else {
            viewExternally(data)
        }
    }

    private fun viewWithCustomTabs(data: URI) {
        SimpleChromeCustomTabs.getInstance()
                .withFallback { viewExternally(data) }
                .withIntentCustomizer { simpleChromeCustomTabsIntentBuilder ->
                    val toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity)
                    simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor)
                }
                .navigateTo(data.toUri(), activity)
    }

    private fun viewExternally(data: URI) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(data.toString())
        activity.startActivity(intent)
    }

    private fun URI.isWebPage() = this.scheme == "http" || this.scheme == "https"
}

