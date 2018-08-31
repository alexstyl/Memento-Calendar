package com.alexstyl.specialdates.debug

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.widget.Toast
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.DebugApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.donate.DebugDonationPreferences
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.peopleevents.DebugPeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsJob
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity
import com.alexstyl.specialdates.support.AskForSupport
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigureActivity
import com.alexstyl.specialdates.wear.WearSyncUpcomingEventsView
import com.evernote.android.job.JobRequest
import java.util.*
import javax.inject.Inject

@Deprecated("Changing to DebugOptionFragment")
class DebugFragment : MementoPreferenceFragment() {


    @Inject lateinit var namedayUserSettings: NamedayUserSettings
    @Inject lateinit var contactsProvider: ContactsProvider
    @Inject lateinit var refresher: UpcomingEventsViewRefresher
    @Inject lateinit var tracker: CrashAndErrorTracker
    @Inject lateinit var monitor: DonateMonitor
    @Inject lateinit var dateParser: DateParser
    @Inject lateinit var peopleEventsUpdater: DebugPeopleEventsUpdater
    @Inject lateinit var askForSupport: AskForSupport



    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        val debugAppComponent = (activity!!.application as DebugApplication).debugAppComponent
        debugAppComponent.inject(this)

        addPreferencesFromResource(R.xml.preference_debug)

        findPreference<Preference>(R.string.key_debug_refresh_db)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            peopleEventsUpdater.refresh()
            toast("Refreshing Database")
            true
        }
        findPreference<Preference>(R.string.key_debug_refresh_widget)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            refresher.refreshViews()
            toast("Widget(s) refreshed")
            true
        }


        findPreference<Preference>(R.string.key_debug_start_calendar)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startDateIntent()
            true
        }

        findPreference<Preference>(R.string.key_debug_trigger_wear_service)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            WearSyncUpcomingEventsView(activity).reloadUpcomingEventsView()
            true
        }
        findPreference<Preference>(R.string.key_debug_reset_donations)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
            DebugDonationPreferences.newInstance(preference.context, monitor).reset()
            Toast.makeText(preference.context, "Donations reset. You should see ads from now on", Toast.LENGTH_SHORT).show()
            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_support)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
            DebugUserOptions.newInstance(preference.context, R.string.pref_call_to_rate).wipe()
            askForSupport.requestForRatingSooner()
            val message = "Support triggered. You should now see a prompt to rate the app when you launch it"
            toast(message)
            true
        }
        findPreference<Preference>(R.string.key_debug_facebook)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(activity, FacebookLogInActivity::class.java)
            startActivity(intent)
            true
        }
        findPreference<Preference>(R.string.key_debug_facebook_fetch_friends)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            JobRequest.Builder(FacebookFriendsJob.TAG)
                    .startNow()
                    .build()
                    .schedule()

            toast("Facebook Friends Job Triggered")
            true
        }

        findPreference<Preference>(R.string.key_debug_open_contact)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val contactPickerIntent = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI
            )
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
            true
        }


        findPreference<Preference>(R.string.key_debug_configure_widgets)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(activity, UpcomingWidgetConfigureActivity::class.java)
            activity!!.startActivity(intent)
            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_crash)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            throw RuntimeException("Ka-boom!")
        }


    }


    private fun startDateIntent() {
        val cal = Calendar.getInstance()
        val builder = CalendarContract.CONTENT_URI.buildUpon()
        builder.appendPath("time")
        ContentUris.appendId(builder, cal.timeInMillis)
        val intent = Intent(Intent.ACTION_VIEW)
                .setData(builder.build())

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, data.data!!.lastPathSegment.toString())
            intent.data = uri
            startActivity(intent)
        }
    }

    companion object {
        private const val RESULT_PICK_CONTACT = 4929
    }
}
