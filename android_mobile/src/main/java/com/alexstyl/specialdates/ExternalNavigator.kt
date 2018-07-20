package com.alexstyl.specialdates

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract.Contacts
import android.widget.Toast

import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.person.BottomSheetIntentDialog
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.ui.base.MementoActivity
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs

import java.util.ArrayList

class ExternalNavigator(
        private val activity: MementoActivity,
        private val analytics: Analytics,
        private val tracker: CrashAndErrorTracker,
        private val attributeExtractor: AttributeExtractor) {
    
    init {
        activity.lifecycle.addObserver(SimpleChromeCustomTabsObserver(activity))
    }

    private fun createPlayStoreUri(): Uri {
        val packageName = activity.packageName
        return Uri.parse("market://details?id=$packageName")
    }

    fun toPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, createPlayStoreUri())
            activity.startActivity(intent)
            analytics.trackScreen(Screen.PLAY_STORE)
        } catch (e: ActivityNotFoundException) {
            tracker.track(e)
        }

    }


    fun connectTo(activity: Activity) {
        SimpleChromeCustomTabs.getInstance().connectTo(activity);
    }

    fun disconnectFrom(activity: Activity) {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(activity);
    }

    fun toContactDetails(contact: Contact) {
        if (contact.source == ContactSource.SOURCE_FACEBOOK) {
            toFacebookContactDetails(contact)
        } else if (contact.source == ContactSource.SOURCE_DEVICE) {
            toDeviceContactDetails(contact)
        } else {
            throw IllegalStateException("Invalid contact source " + contact.source)
        }
    }


    private fun toFacebookContactDetails(contact: Contact) {
        // TODO facebook app first
        // fallback Chrome tabs

        val facebookUri = Uri.parse("https://www.facebook.com/" + contact.contactID)
        SimpleChromeCustomTabs.getInstance()
                .withFallback { openExternally(facebookUri) }
                .withIntentCustomizer { simpleChromeCustomTabsIntentBuilder ->
                    val toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity)
                    simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor)
                }
                .navigateTo(facebookUri, activity)


    }

    private fun openExternally(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            activity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun toDeviceContactDetails(contact: Contact) {
        try {
            val intents = viewContactIntentsFromOtherApps(contact)
            if (intents.size == 1) {
                activity.startActivity(intents[0])
            } else if (intents.size > 1) {
                // show bottom sheet with options
                val bottomSheetPicturesDialog = BottomSheetIntentDialog.newIntent(
                        activity.getString(R.string.View_contact),
                        intents)
                bottomSheetPicturesDialog.show(activity.supportFragmentManager, "CONTACT")
            }
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity,
                    R.string.no_app_found,
                    Toast.LENGTH_SHORT).show()
        }

    }

    private fun viewContactIntentsFromOtherApps(contact: Contact): ArrayList<Intent> {
        val packageManager = activity.packageManager

        val intent = viewContact(contact)
        val activities = packageManager.queryIntentActivities(intent, 0)
        val myPackage = activity.packageName
        val targetIntents = ArrayList<Intent>()
        for (currentInfo in activities) {
            val packageName = currentInfo.activityInfo.packageName
            if (myPackage != packageName) {
                val targetIntent = viewContact(contact)
                intent.`package` = packageName
                targetIntent.setClassName(packageName, currentInfo.activityInfo.name)
                targetIntents.add(targetIntent)
            }
        }
        return targetIntents
    }

    private fun viewContact(contact: Contact): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contact.contactID.toString())
        intent.data = uri
        return intent
    }

    fun toFacebookPage() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.facebook.com/memento.calendar/")
            activity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity,
                    R.string.no_app_found,
                    Toast.LENGTH_SHORT)
                    .show()
        }

        analytics.trackScreen(Screen.FACEBOOK_PAGE)
    }
}

