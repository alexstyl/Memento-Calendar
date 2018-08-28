package com.alexstyl.specialdates.debug.dailyreminder

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.support.annotation.RequiresApi
import com.alexstyl.specialdates.DebugApplication
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.AndroidDailyReminderDebugLauncher
import com.alexstyl.specialdates.dailyreminder.ContactEventNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModelFactory
import com.alexstyl.specialdates.dailyreminder.NamedaysNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.log.DailyReminderLogger
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.calendar.ImmutableNamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DebugDailyReminderFragment : MementoPreferenceFragment() {

    @Inject lateinit var notifier: DailyReminderNotifier
    @Inject lateinit var dailyReminderViewModelFactory: DailyReminderViewModelFactory
    @Inject lateinit var dateLabelCreator: DateLabelCreator
    @Inject lateinit var dailyReminderLogger: DailyReminderLogger

    private val dailyReminderLauncher by lazy {
        AndroidDailyReminderDebugLauncher(context!!)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        (activity!!.applicationContext as DebugApplication).debugAppComponent.inject(this)

        addPreferencesFromResource(R.xml.preference_debug_dailyreminder)

        onPreferenceClick(R.string.key_debug_daily_reminder_trigger) {
            triggerDailyReminderOn(todaysDate())
        }

        onPreferenceClick(R.string.key_debug_daily_reminder_trigger_on_date) {
            val today = todaysDate()
            val datePickerDialog = DatePickerDialog(
                    activity!!, DatePickerDialog.OnDateSetListener { _, year, zeroIndexedMonth, dayOfMonth ->
                val month = zeroIndexedMonth + 1
                triggerDailyReminderOn(dateOn(dayOfMonth, month, year))
            },
                    today.year!!, today.month - 1, today.dayOfMonth
            )
            datePickerDialog.show()
        }

        onPreferenceClick(R.string.key_debug_trigger_daily_reminder_notification_one) {
            notifyForContacts(arrayListOf(
                    contactEventOn(todaysDate().minusDay(365 * 10), Contact(123L, "Peter".toDisplayName(), "content://com.android.contacts/contacts/123", ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY)
            ))
        }

        onPreferenceClick(R.string.key_debug_trigger_daily_reminder_notification_many) {
            notifyForContacts(arrayListOf(
                    contactEventOn(todaysDate().minusDay(365 * 10), Contact(336L, "Peter".toDisplayName(), "content://com.android.contacts/contacts/336", ContactSource.SOURCE_DEVICE), StandardEventType.NAMEDAY),
                    contactEventOn(todaysDate().minusDay(365 * 10), Contact(123L, "Alex".toDisplayName(), "content://com.android.contacts/contacts/123", ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY),
                    contactEventOn(todaysDate().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), "content://com.android.contacts/contacts/108", ContactSource.SOURCE_DEVICE), StandardEventType.ANNIVERSARY),
                    contactEventOn(todaysDate().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), "content://com.android.contacts/contacts/108", ContactSource.SOURCE_DEVICE), StandardEventType.OTHER)
            ))
        }

        onPreferenceClick(R.string.key_debug_trigger_namedays_notification) {
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
        }

        onPreferenceClick(R.string.key_debug_trigger_bank_holiday) {
            notifier.notifyFor(
                    DailyReminderViewModel(
                            dailyReminderViewModelFactory.summaryOf(emptyList()),
                            emptyList(),
                            Optional.absent(),
                            bankholidayNotification()
                    )
            )
        }

        onPreferenceClick(R.string.key_debug_daily_reminder_logs_clear) {
            Observable.fromCallable {
                dailyReminderLogger.clearAll()
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        showDailyReminderLogs()
                    }
        }

        onPreferenceClick(R.string.key_debug_daily_reminder_logs_refresh) {
            showDailyReminderLogs()
        }

        showDailyReminderLogs()
    }


    private fun triggerDailyReminderOn(date: Date) {
        dailyReminderLauncher.launchForDate(date)
        toast("Daily Reminder Triggered")
    }


    private fun showDailyReminderLogs() {
        val logPreferences = findPreference<Preference>(R.string.key_debug_daily_reminder_logs)!!
        Observable.fromCallable {
            dailyReminderLogger.fetchAllEvents()
        }.map {
            if (it.isEmpty()) {
                "Daily Reminder hasn't run yet"
            } else {
                it
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lines ->
                    logPreferences.summary = lines
                }
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

    private fun bankholidayNotification() = Optional(dailyReminderViewModelFactory.viewModelFor(BankHoliday("Test Bank Holiday", todaysDate())))

    private fun namedaysNotifications(names: ArrayList<String>): Optional<NamedaysNotificationViewModel> =
            Optional(dailyReminderViewModelFactory.viewModelFor(ImmutableNamesInADate(todaysDate(), names)))

    private fun contactEventOn(date: Date, contact: Contact, standardEventType: StandardEventType) = ContactEvent(Optional.absent(), standardEventType,
            date, contact)

    private fun ArrayList<ContactEvent>.toViewModels(): ArrayList<ContactEventNotificationViewModel> {
        val viewModels = arrayListOf<ContactEventNotificationViewModel>()
        forEach {
            viewModels.add(dailyReminderViewModelFactory.viewModelFor(it.contact, listOf(it)))
        }
        return viewModels
    }

    private fun String.toDisplayName(): DisplayName = DisplayName.from(this)
}
