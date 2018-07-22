package com.alexstyl.specialdates.debug.dailyreminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.Preference
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.ContactEventNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences
import com.alexstyl.specialdates.dailyreminder.DailyReminderJob
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModelFactory
import com.alexstyl.specialdates.dailyreminder.NamedaysNotificationViewModel
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.evernote.android.job.JobRequest
import java.net.URI
import javax.inject.Inject

class DebugDailyReminderFragment : MementoPreferenceFragment() {

    @Inject lateinit var dailyReminderDebugPreferences: DailyReminderDebugPreferences
    @Inject lateinit var notifier: DailyReminderNotifier
    @Inject lateinit var dailyReminderViewModelFactory: DailyReminderViewModelFactory

    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        addPreferencesFromResource(R.xml.preference_debug_dailyreminder)

        dailyReminderDebugPreferences = DailyReminderDebugPreferences.newInstance(activity!!)

        findPreference<Preference>(R.string.key_debug_daily_reminder)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            JobRequest.Builder(DailyReminderJob.TAG)
                    .startNow()
                    .build()
                    .schedule()

            toast("Daily Reminder Triggered")
            true
        }

        findPreference<Preference>(R.string.key_debug_daily_reminder_date_enable)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            dailyReminderDebugPreferences.setEnabled(newValue as Boolean)
            true
        }

        findPreference<Preference>(R.string.key_debug_daily_reminder_date)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val today = dailyReminderDebugPreferences.selectedDate
            val datePickerDialog = DatePickerDialog(
                    activity!!, onDailyReminderDateSelectedListener,
                    today.year, today.month - 1, today.dayOfMonth
            )
            datePickerDialog.show()
            false
        }
        findPreference<Preference>(R.string.key_debug_trigger_daily_reminder_notification_one)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifyForContacts(arrayListOf(
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(123L, "Peter".toDisplayName(), URI.create("content://com.android.contacts/contacts/123"), ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY)
            ))

            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_daily_reminder_notification_many)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifyForContacts(arrayListOf(
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(336L, "Peter".toDisplayName(), URI.create("content://com.android.contacts/contacts/336"), ContactSource.SOURCE_DEVICE), StandardEventType.NAMEDAY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(123L, "Alex".toDisplayName(), URI.create("content://com.android.contacts/contacts/123"), ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), ContactSource.SOURCE_DEVICE), StandardEventType.ANNIVERSARY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), ContactSource.SOURCE_DEVICE), StandardEventType.OTHER)
            ))

            true
        }

        findPreference<Preference>(R.string.key_debug_trigger_namedays_notification)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifier.notifyFor(
                    DailyReminderViewModel(
                            dailyReminderViewModelFactory.summaryOf(emptyList()),
                            emptyList(),
                            namedaysNotifications(
                                    arrayListOf("NamedayTest", "Alex", "Bravo", "NamedaysRock"
                                            , "Alex", "Bravo", "NamedaysRock"
                                            , "Alex", "Bravo", "NamedaysRock"
                                            , "Alex", "Bravo", "NamedaysRock")),
                            Optional.absent()
                    )
            )
            true
        }
        findPreference<Preference>(R.string.key_debug_trigger_bank_holiday)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifier.notifyFor(
                    DailyReminderViewModel(
                            dailyReminderViewModelFactory.summaryOf(emptyList()),
                            emptyList(),
                            Optional.absent(),
                            bankholidayNotification()
                    )
            )
            true
        }
    }

    private val onDailyReminderDateSelectedListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val month1 = month + 1 // dialog picker months have 0 index
        dailyReminderDebugPreferences.setSelectedDate(dayOfMonth, month1, year)
    }

    private fun notifyForContacts(contacts: ArrayList<ContactEvent>) {
        val viewModels = contacts.toViewModels()

        notifier.notifyFor(
                DailyReminderViewModel(
                        dailyReminderViewModelFactory.summaryOf(viewModels),
                        viewModels,
                        Optional.absent(),
                        Optional.absent()
                )
        )
    }

    private fun bankholidayNotification() = Optional(dailyReminderViewModelFactory.viewModelFor(BankHoliday("Test Bank Holiday", Date.today())))

    private fun namedaysNotifications(arrayList: ArrayList<String>): Optional<NamedaysNotificationViewModel> =
            Optional(dailyReminderViewModelFactory.viewModelFor(NamesInADate(Date.today(),
                    arrayList
            )))

    private fun contactEventOn(date: Date, contact: Contact, standardEventType: StandardEventType) = ContactEvent(Optional.absent(), standardEventType,
            date, contact)

    private fun ArrayList<ContactEvent>.toViewModels(): ArrayList<ContactEventNotificationViewModel> {
        val viewmodels = arrayListOf<ContactEventNotificationViewModel>()
        forEach {
            viewmodels.add(dailyReminderViewModelFactory.viewModelFor(it.contact, listOf(it)))
        }
        return viewmodels
    }

    private fun String.toDisplayName(): DisplayName = DisplayName.from(this)
}
