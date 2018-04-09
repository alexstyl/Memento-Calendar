package com.alexstyl.specialdates.debug

import android.app.Activity
import android.app.DatePickerDialog
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
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.ContactEventNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences
import com.alexstyl.specialdates.dailyreminder.DailyReminderIntentService
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModel
import com.alexstyl.specialdates.dailyreminder.NotificationViewModelFactory
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.donate.DebugDonationPreferences
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.peopleevents.DebugPeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsSettings
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsIntentService
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity
import com.alexstyl.specialdates.support.AskForSupport
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.alexstyl.specialdates.wear.WearSyncUpcomingEventsView
import java.net.URI
import java.util.Calendar
import javax.inject.Inject

class DebugFragment : MementoPreferenceFragment() {

    var dailyReminderDebugPreferences: DailyReminderDebugPreferences? = null
        @Inject set
    var namedayUserSettings: NamedayUserSettings? = null
        @Inject set
    var contactsProvider: ContactsProvider? = null
        @Inject set
    var refresher: UpcomingEventsViewRefresher? = null
        @Inject set
    var tracker: CrashAndErrorTracker? = null
        @Inject set
    var monitor: DonateMonitor? = null
        @Inject set
    var upcomingEventsSettings: UpcomingEventsSettings? = null
        @Inject set
    var dateParser: DateParser? = null
        @Inject set
    var notifier: DailyReminderNotifier? = null
        @Inject set
    var peopleEventsUpdater: DebugPeopleEventsUpdater? = null
        @Inject set
    var notificationViewModelFactory: NotificationViewModelFactory? = null
        @Inject set

    private val onDailyReminderDateSelectedListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val month1 = month + 1 // dialog picker months have 0 index
        dailyReminderDebugPreferences!!.setSelectedDate(dayOfMonth, month1, year)
    }

    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        val debugAppComponent = (activity!!.application as DebugApplication).debugAppComponent
        debugAppComponent.inject(this)

        addPreferencesFromResource(R.xml.preference_debug)
        dailyReminderDebugPreferences = DailyReminderDebugPreferences.newInstance(activity)
        findPreference<Preference>(R.string.key_debug_refresh_db)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            peopleEventsUpdater?.refresh()
            showToast("Refreshing Database")
            true
        }
        findPreference<Preference>(R.string.key_debug_refresh_widget)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            refresher!!.refreshViews()
            showToast("Widget(s) refreshed")
            true
        }

        findPreference<Preference>(R.string.key_debug_daily_reminder_date_enable)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            dailyReminderDebugPreferences!!.setEnabled(newValue as Boolean)
            true
        }
        findPreference<Preference>(R.string.key_debug_daily_reminder_date)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val today = dailyReminderDebugPreferences!!.selectedDate
            val datePickerDialog = DatePickerDialog(
                    activity!!, onDailyReminderDateSelectedListener,
                    today.year, today.month - 1, today.dayOfMonth
            )
            datePickerDialog.show()
            false
        }

        findPreference<Preference>(R.string.key_debug_daily_reminder)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {

            val service = Intent(context, DailyReminderIntentService::class.java)
            activity?.startService(service)

            showToast("Daily Reminder Triggered")
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
            DebugPreferences.newInstance(preference.context, R.string.pref_call_to_rate).wipe()
            AskForSupport(preference.context).requestForRatingSooner()
            val message = "Support triggered. You should now see a prompt to rate the app when you launch it"
            showToast(message)
            true
        }
        findPreference<Preference>(R.string.key_debug_facebook)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(activity, FacebookLogInActivity::class.java)
            startActivity(intent)
            true
        }
        findPreference<Preference>(R.string.key_debug_facebook_fetch_friends)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(activity, FacebookFriendsIntentService::class.java)
            activity!!.startService(intent)
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
        findPreference<Preference>(R.string.key_debug_trigger_daily_reminder_notification)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val eventDate = Date.today().minusDay(365 * 10)

            val viewModels = arrayListOf(
                    contactEventOn(eventDate, Contact(336L, "Διονύσης Μόνε".toDisplayName(), URI.create("content://com.android.contacts/contacts/336"), SOURCE_DEVICE), StandardEventType.NAMEDAY),
                    contactEventOn(eventDate, Contact(123L, "Χρήστος Πλατάκης".toDisplayName(), URI.create("content://com.android.contacts/contacts/123"), SOURCE_DEVICE), StandardEventType.BIRTHDAY),
                    contactEventOn(eventDate, Contact(108L, "Μαριάννα".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), SOURCE_DEVICE), StandardEventType.ANNIVERSARY),
                    contactEventOn(eventDate, Contact(108L, "Μαριάννα".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), SOURCE_DEVICE), StandardEventType.OTHER)
            ).toViewModels()

            notifier!!.notifyFor(
                    DailyReminderViewModel(
                            notificationViewModelFactory!!.summaryOf(viewModels),
                            viewModels,
                            Optional.absent(),
                            Optional.absent()
                    )
            )

            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_namedays_notification)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifier!!.notifyFor(
                    DailyReminderViewModel(
                            notificationViewModelFactory!!.summaryOf(emptyList()),
                            emptyList(),
                            namedaysNotifications(),
                            Optional.absent()
                    )
            )
            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_bank_holiday)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifier!!.notifyFor(
                    DailyReminderViewModel(
                            notificationViewModelFactory!!.summaryOf(emptyList()),
                            emptyList(),
                            Optional.absent(),
                            bankholidayNotification()
                    )
            )
            true
        }
    }

    private fun bankholidayNotification() = Optional(notificationViewModelFactory!!.viewModelFor(BankHoliday("Test Bank Holiday", Date.today())))

    private fun namedaysNotifications() =
            Optional(notificationViewModelFactory!!.viewModelFor(NamesInADate(Date.today(), arrayListOf("NamedayTest", "Alex", "Bravo", "NamedaysRock"))))

    private fun contactEventOn(date: Date, contact: Contact, standardEventType: StandardEventType) = ContactEvent(Optional.absent(), standardEventType,
            date, contact)

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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

    private fun String.toDisplayName(): DisplayName = DisplayName.from(this)

    private fun ArrayList<ContactEvent>.toViewModels(): ArrayList<ContactEventNotificationViewModel> {
        val viewmodels = arrayListOf<ContactEventNotificationViewModel>()
        forEach {
            viewmodels.add(notificationViewModelFactory!!.viewModelFor(it))
        }
        return viewmodels
    }
}
