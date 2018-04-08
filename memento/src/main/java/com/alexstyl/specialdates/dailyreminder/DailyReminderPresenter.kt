package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class DailyReminderPresenter(private var permissions: MementoPermissions,
                             private var peopleEventsProvider: PeopleEventsProvider,
                             private var namedayPreferences: NamedayUserSettings,
                             private var bankHolidaysUserSettings: BankHolidaysUserSettings,
                             private var namedayCalendarProvider: NamedayCalendarProvider,
                             private var factory: NotificationViewModelFactory,
                             private var errorTracker: CrashAndErrorTracker,
                             private var bankHolidayProvider: BankHolidayProvider,

                             private val workScheduler: Scheduler,
                             private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: DailyReminderView) {
        val today = Date.today()

        disposable =
                contactEvents(today)
                        .map {
                            DailyReminderViewModel(factory.summaryOf(it), it)
                        }
                        .observeOn(resultScheduler)
                        .doOnError { error ->
                            errorTracker.track(error)
                        }
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels ->
                            view.show(viewModels)
                        }
    }

    private fun contactEvents(date: Date) =
            Observable.fromCallable {
                if (permissions.canReadContacts()) {
                    peopleEventsProvider.fetchEventsOn(date)
                } else {
                    ContactEventsOnADate.createFrom(date, emptyList())
                }
            }.map {
                it.events.map { contactEvent ->
                    factory.viewModelFor(contactEvent)
                }
            }


//    private fun namedays(date: Date) = Observable.fromCallable {
//        if (namedayPreferences.isEnabled) {
//            val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(namedayPreferences.selectedLanguage, date.year)
//            namedayCalendar.getAllNamedaysOn(date)
//        } else {
//            NamesInADate(date, ArrayList())
//        }
//    }.map {
//        factory.namedaysViewModel(it)
//    }


//    private fun bankholidays(date: Date): Observable<BankHolidayNotificationViewModel?> = Observable.fromCallable {
//        if (bankHolidaysUserSettings.isEnabled) {
//            bankHolidayProvider.calculateBankHolidayOn(date)
//        } else {
//            null
//        }
//    }.map { maybeBankholiday ->
//        factory.forBankHoliday(maybeBankholiday)
//    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}
