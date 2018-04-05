package com.alexstyl.specialdates.dailyreminder

import android.app.IntentService
import android.content.Intent
import com.alexstyl.android.AlarmManagerCompat
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.bankholidays.AndroidBankHolidaysPreferences
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.AndroidPermissions
import com.novoda.notils.logger.simple.Log
import javax.inject.Inject

/**
 * A service that looks up all events on the specified date and notifies the user about it
 */
class DailyReminderIntentService : IntentService("DailyReminder") {

    private var bankHolidaysPreferences: AndroidBankHolidaysPreferences? = null
    private var checker: AndroidPermissions? = null

    var namedayCalendarProvider: NamedayCalendarProvider? = null
        @Inject set
    var namedayPreferences: NamedayUserSettings? = null
        @Inject set
    var notifier: DailyReminderNotifier? = null
        @Inject set
    var peopleEventsProvider: PeopleEventsProvider? = null
        @Inject set
    var tracker: CrashAndErrorTracker? = null
        @Inject set

    override fun onCreate() {
        super.onCreate()

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
        bankHolidaysPreferences = AndroidBankHolidaysPreferences.newInstance(this)
        checker = AndroidPermissions(tracker, this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val today = dayDateToDisplay

        if (hasContactPermission()) {
            val celebrationDate = peopleEventsProvider!!.fetchEventsBetween(TimePeriod.between(today, today))
            if (celebrationDate.isNotEmpty()) {
                notifier!!.forDailyReminder(today, celebrationDate)
                // notifier!!.forDailyReminderAll(today, celebrationDate)
            }
        }

        if (namedaysAreEnabledForAllCases()) {
            notifyForNamedaysFor(today)
        }
        if (bankholidaysAreEnabled()) {
            notifyForBankholidaysFor(today)
        }

        val preferences = DailyReminderPreferences.newInstance(this)
        if (preferences.isEnabled) {
            DailyReminderScheduler(AlarmManagerCompat.from(this), this).setupReminder(preferences)
        }
    }

    private val dayDateToDisplay: Date
        get() {
            if (BuildConfig.DEBUG) {
                val preferences = DailyReminderDebugPreferences.newInstance(this)
                if (preferences.isFakeDateEnabled) {
                    val selectedDate = preferences.selectedDate
                    Log.d("Using DEBUG date to display: $selectedDate")
                    return selectedDate
                }
            }
            return Date.today()
        }

    private fun hasContactPermission(): Boolean {
        return checker!!.canReadAndWriteContacts()
    }

    private fun namedaysAreEnabledForAllCases(): Boolean {
        return namedayPreferences!!.isEnabled && !namedayPreferences!!.isEnabledForContactsOnly
    }

    private fun notifyForNamedaysFor(date: Date) {
        val locale = namedayPreferences!!.selectedLanguage
        val namedayCalendar = namedayCalendarProvider!!.loadNamedayCalendarForLocale(locale, date.year)
        val names = namedayCalendar.getAllNamedaysOn(date)
        if (containsNames(names)) {
            notifier!!.forNamedays(names.names, date)
        }
    }

    private fun bankholidaysAreEnabled(): Boolean {
        return bankHolidaysPreferences!!.isEnabled
    }

    private fun notifyForBankholidaysFor(date: Date) {
        val bankHoliday = findBankholidayFor(date)
        if (bankHoliday.isPresent) {
            notifier!!.forBankholiday(bankHoliday.get())
        }
    }

    private fun findBankholidayFor(date: Date): Optional<BankHoliday> {
        val provider = BankHolidayProvider(GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE))
        return provider.calculateBankHolidayOn(date)
    }

    private fun containsNames(names: NamesInADate): Boolean {
        return names.nameCount() > 0
    }
}
