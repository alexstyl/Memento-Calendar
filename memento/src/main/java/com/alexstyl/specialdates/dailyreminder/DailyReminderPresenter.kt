package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.dailyreminder.log.DailyReminderLogger
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NoNamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3

class DailyReminderPresenter(private var permissions: MementoPermissions,
                             private var peopleEventsProvider: PeopleEventsProvider,
                             private var namedayPreferences: NamedayUserSettings,
                             private var bankHolidaysUserSettings: BankHolidaysUserSettings,
                             private var namedayCalendarProvider: NamedayCalendarProvider,
                             private var factory: DailyReminderViewModelFactory,
                             private var errorTracker: CrashAndErrorTracker,
                             private var bankHolidayProvider: BankHolidayProvider,
                             private var analytics: Analytics,
                             private var workScheduler: Scheduler,
                             private var resultScheduler: Scheduler,
                             private var dailyReminderLogger: DailyReminderLogger) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: DailyReminderView, date: Date) {
        disposable =
                Observable.zip(
                        contactEvents(date),
                        namedays(date),
                        bankholidays(date),
                        Function3 { t1: List<ContactEventNotificationViewModel>,
                                    t2: Optional<NamedaysNotificationViewModel>,
                                    t3: Optional<BankHolidayNotificationViewModel> ->
                            DailyReminderViewModel(factory.summaryOf(t1), t1, t2, t3).apply {
                                dailyReminderLogger.appendEvents(date, this)
                            }
                        })
                        .doOnNext { analytics.trackDailyReminderTriggered() }
                        .doOnError { it -> errorTracker.track(it) }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { viewModels -> view.show(viewModels) }
    }

    private fun contactEvents(date: Date) =
            Observable.fromCallable {
                contactEventsOn(date)
            }.map { events ->
                val list = arrayListOf<ContactEventNotificationViewModel>()
                val grouped = events.groupBy { it.contact }

                grouped.keys.forEach { contact ->
                    val events2 = grouped[contact]!!
                    list.add(factory.viewModelFor(contact, events2))
                }
                list.toList()
            }

    private fun contactEventsOn(date: Date): List<ContactEvent> {
        return if (permissions.canReadContacts()) {
            peopleEventsProvider.fetchEventsOn(date)
        } else {
            emptyList()
        }
    }


    private fun namedays(date: Date): Observable<Optional<NamedaysNotificationViewModel>> = Observable.fromCallable {
        if (namedayPreferences.isEnabled) {
            val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(namedayPreferences.selectedLanguage, date.year!!)
            namedayCalendar.getAllNamedaysOn(date)
        } else {
            NoNamesInADate(date)
        }
    }.map {
        if (it.names.isEmpty()) {
            Optional.absent()
        } else {
            Optional(factory.viewModelFor(it))
        }
    }


    private fun bankholidays(date: Date): Observable<Optional<BankHolidayNotificationViewModel>> = Observable.fromCallable {
        if (bankHolidaysUserSettings.isEnabled) {
            val calculateBankHolidayOn = bankHolidayProvider.calculateBankHolidayOn(date)
            if (calculateBankHolidayOn != null) {
                val viewModel = factory.viewModelFor(calculateBankHolidayOn)
                return@fromCallable Optional(viewModel)
            }
        }
        Optional.absent<BankHolidayNotificationViewModel>()
    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}
