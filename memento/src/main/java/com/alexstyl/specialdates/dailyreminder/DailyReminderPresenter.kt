package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate
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
                             private var factory: NotificationViewModelFactory,
                             private var errorTracker: CrashAndErrorTracker,
                             private var bankHolidayProvider: BankHolidayProvider,

                             private val workScheduler: Scheduler,
                             private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: DailyReminderView) {
        val today = Date.today()

        disposable =
                Observable.zip(
                        contactEvents(today),
                        namedays(today),
                        bankholidays(today),
                        Function3({ t1: List<ContactEventNotificationViewModel>,
                                    t2: Optional<NamedaysNotificationViewModel>,
                                    t3: Optional<BankHolidayNotificationViewModel> ->

                            DailyReminderViewModel(factory.summaryOf(t1), t1, t2, t3)
                        }))

                        .observeOn(resultScheduler)
                        .doOnError { it ->
                            errorTracker.track(it)
                        }
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels: DailyReminderViewModel ->
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


    private fun namedays(date: Date): Observable<Optional<NamedaysNotificationViewModel>> = Observable.fromCallable {
        if (namedayPreferences.isEnabled) {
            val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(namedayPreferences.selectedLanguage, date.year)
            namedayCalendar.getAllNamedaysOn(date)
        } else {
            NamesInADate(date, ArrayList())
        }
    }.map {
        if (it.getNames().isEmpty()) {
            Optional.absent<NamedaysNotificationViewModel>()
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
