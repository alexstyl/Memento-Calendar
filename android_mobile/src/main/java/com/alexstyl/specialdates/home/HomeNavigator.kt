package com.alexstyl.specialdates.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.ShareAppIntentCreator
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.addevent.AddEventActivity
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.donate.DonateActivity
import com.alexstyl.specialdates.events.namedays.activity.NamedaysOnADayActivity
import com.alexstyl.specialdates.facebook.FacebookProfileActivity
import com.alexstyl.specialdates.facebook.FacebookUserSettings
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity
import com.alexstyl.specialdates.permissions.ContactPermissionActivity
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.search.SearchActivity
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs

class HomeNavigator(private val analytics: Analytics,
                    private val strings: Strings,
                    private val facebookUserSettings: FacebookUserSettings,
                    private val tracker: CrashAndErrorTracker,
                    private val attributeExtractor: AttributeExtractor) {

    fun toDonate(activity: Activity) {
        if (hasPlayStoreInstalled(activity)) {
            val intent = DonateActivity.createIntent(activity)
            activity.startActivity(intent)
        } else {
            SimpleChromeCustomTabs.getInstance()
                    .withFallback { navigateToDonateWebsite(activity) }
                    .withIntentCustomizer { simpleChromeCustomTabsIntentBuilder ->
                        val toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity)
                        simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor)
                    }
                    .navigateTo(SUPPORT_URL, activity)

        }
        analytics.trackScreen(Screen.DONATE)
    }

    private fun hasPlayStoreInstalled(activity: Activity): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun navigateToDonateWebsite(activity: Activity) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = SUPPORT_URL
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            tracker.track(e)
        }
    }

    fun toAddEvent(activity: Activity, code: Int) {
        val intent = Intent(activity, AddEventActivity::class.java)
        activity.startActivityForResult(intent, code)
    }

    fun toFacebookImport(activity: Activity) {
        if (facebookUserSettings.isLoggedIn) {
            val intent = Intent(activity, FacebookProfileActivity::class.java)
            activity.startActivity(intent)
        } else {
            val intent = Intent(activity, FacebookLogInActivity::class.java)
            activity.startActivity(intent)
        }
    }

    fun toSearch(activity: Activity) {
        val intent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(intent)
        analytics.trackScreen(Screen.SEARCH)
    }

    fun toDateDetails(dateSelected: Date, activity: Activity) {
        val intent = NamedaysOnADayActivity.getStartIntent(activity, dateSelected)
        activity.startActivity(intent)
        analytics.trackScreen(Screen.DATE_DETAILS)
    }

    fun toAppInvite(activity: Activity) {
        val intent = ShareAppIntentCreator(strings).buildIntent()
        val shareTitle = strings.inviteFriend()
        activity.startActivity(Intent.createChooser(intent, shareTitle))
        analytics.trackAppInviteRequested()
    }

    fun toGithubPage(activity: Activity) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://github.com/alexstyl/Memento-Calendar")
        analytics.trackVisitGithub()
        activity.startActivity(intent)
    }

    fun toContactDetails(contact: Contact, activity: Activity) {
        val intent = PersonActivity.buildIntentFor(activity, contact)
        activity.startActivity(intent)
        analytics.trackContactDetailsViewed(contact)
    }

    fun toContactPermission(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, ContactPermissionActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
        analytics.trackScreen(Screen.CONTACT_PERMISSION_REQUESTED)
    }

    companion object {
        private val SUPPORT_URL = Uri.parse("https://g3mge.app.goo.gl/jdF1")
    }
}
