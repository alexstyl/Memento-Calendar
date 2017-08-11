package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

internal class NamedayPresenter(private val namedayCalendar: NamedayCalendar,
                                private val namedaysViewModelFactory: NamedaysViewModelFactory,
                                private val workScheduler: Scheduler,
                                private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresenting(into: NamedaysMVPView, forDate: Date) {
        disposable = Observable.fromCallable { namedayCalendar.getAllNamedayOn(forDate) }
                .observeOn(workScheduler) // TODO does this mean that the fromCallable will be executed on the workScheduler?
                .map { it.names.asViewModels() }
                .observeOn(resultScheduler)
                .subscribe {
                    namedaysViewModel ->
                    into.displayNamedays(namedaysViewModel)
                }
    }

    fun stopPresenting() {
        disposable?.dispose()
    }

    private fun List<String>.asViewModels(): ArrayList<NamedaysViewModel> {
        val list = ArrayList<NamedaysViewModel>()
        this.forEach {
            val viewModel = namedaysViewModelFactory.viewModelsFor(it)
            list.add(viewModel)
        }
        return list
    }

}
