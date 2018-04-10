package com.alexstyl.specialdates.dailyreminder.actions

import com.alexstyl.specialdates.person.ContactActions
import com.alexstyl.specialdates.person.PersonActionsProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class ContactActionsPresenter(
        private val personActionsProvider: PersonActionsProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingCallsInto(view: ContactActionsView, contactActions: ContactActions) {
        disposable =
                personActionsProvider.getCallsFor(view.contact(), contactActions)
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        }

    }

    fun startPresentingMessagingInto(view: ContactActionsView, contactActions: ContactActions) {
        disposable =
                personActionsProvider.getMessagesFor(view.contact(), contactActions)
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        }

    }

    fun stopPresenting() {
        disposable?.dispose()
    }

}
