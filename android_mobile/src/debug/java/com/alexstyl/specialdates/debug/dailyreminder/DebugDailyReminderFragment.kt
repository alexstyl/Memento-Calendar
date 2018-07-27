package com.alexstyl.specialdates.debug.dailyreminder

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.support.annotation.RequiresApi
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.ContactEventNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderJob
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModelFactory
import com.alexstyl.specialdates.dailyreminder.NamedaysNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.log.DailyReminderLogger
import com.alexstyl.specialdates.dailyreminder.putDate
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.URI
import javax.inject.Inject

class DebugDailyReminderFragment : MementoPreferenceFragment() {

    @Inject lateinit var notifier: DailyReminderNotifier
    @Inject lateinit var dailyReminderViewModelFactory: DailyReminderViewModelFactory
    @Inject lateinit var dateLabelCreator: DateLabelCreator
    @Inject lateinit var dailyReminderLogger: DailyReminderLogger


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        (activity!!.applicationContext as MementoApplication).applicationModule.inject(this)

        addPreferencesFromResource(R.xml.preference_debug_dailyreminder)

        onPreferenceClick(R.string.key_debug_daily_reminder_trigger) {
            triggerDailyReminderOn(Date.today())
        }

        onPreferenceClick(R.string.key_debug_daily_reminder_trigger_on_date) {
            val today = Date.today()
            val datePickerDialog = DatePickerDialog(
                    activity!!, DatePickerDialog.OnDateSetListener { _, year, zeroIndexedMonth, dayOfMonth ->
                val month = zeroIndexedMonth + 1
                triggerDailyReminderOn(Date.on(dayOfMonth, month, year))
            },
                    today.year, today.month - 1, today.dayOfMonth
            )
            datePickerDialog.show()
        }

        findPreference<Preference>(R.string.key_debug_trigger_daily_reminder_notification_one)!!
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            notifyForContacts(arrayListOf(
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(123L, "Peter".toDisplayName(), URI.create("content://com.android.contacts/contacts/123"), ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY)
            ))

            true
        }

        onPreferenceClick(R.string.key_debug_trigger_daily_reminder_notification_many) {
            notifyForContacts(arrayListOf(
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(336L, "Peter".toDisplayName(), URI.create("content://com.android.contacts/contacts/336"), ContactSource.SOURCE_DEVICE), StandardEventType.NAMEDAY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(123L, "Alex".toDisplayName(), URI.create("content://com.android.contacts/contacts/123"), ContactSource.SOURCE_DEVICE), StandardEventType.BIRTHDAY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), ContactSource.SOURCE_DEVICE), StandardEventType.ANNIVERSARY),
                    contactEventOn(Date.today().minusDay(365 * 10), Contact(108L, "Anna".toDisplayName(), URI.create("content://com.android.contacts/contacts/108"), ContactSource.SOURCE_DEVICE), StandardEventType.OTHER)
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

    private fun triggerDailyReminderOn(date: Date?) {
        DailyJob.startNowOnce(
                JobRequest.Builder(DailyReminderJob.TAG)
                        .apply {
                            if (date != null) {
                                this.addExtras(PersistableBundleCompat().apply {
                                    putDate(date)
                                })
                            }
                        }
        )

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

    private fun bankholidayNotification() = Optional(dailyReminderViewModelFactory.viewModelFor(BankHoliday("Test Bank Holiday", Date.today())))

    private fun namedaysNotifications(arrayList: ArrayList<String>): Optional<NamedaysNotificationViewModel> =
            Optional(dailyReminderViewModelFactory.viewModelFor(NamesInADate(Date.today(),
                    arrayList
            )))

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
