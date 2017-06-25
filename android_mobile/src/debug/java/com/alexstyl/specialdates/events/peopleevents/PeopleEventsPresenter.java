package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.Monitor;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

class PeopleEventsPresenter {

    private final PeopleEventsUpdater peopleEventsUpdater;
    private final Scheduler resultScheduler;
    private final EventsRefreshRequestsMonitor monitor;
    private final PeopleEventsViewRefresher viewRefresher;

    private CompositeDisposable disposable = new CompositeDisposable();

    PeopleEventsPresenter(Scheduler resultScheduler,
                          EventsRefreshRequestsMonitor monitor,
                          PeopleEventsUpdater peopleEventsUpdater,
                          PeopleEventsViewRefresher viewRefresher) {
        this.peopleEventsUpdater = peopleEventsUpdater;
        this.resultScheduler = resultScheduler;
        this.monitor = monitor;
        this.viewRefresher = viewRefresher;
    }

    void present() {
        monitor.startObserving(new Monitor.Callback() {
            @Override
            public void onMonitorTriggered() {
                refreshEvents();
            }
        });
    }

    private void refreshEvents() {
        disposable.add(
                Completable.fromAction(updateEvents())
                        .doOnComplete(refreshViews())
                        .observeOn(resultScheduler)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }

    private Action updateEvents() {
        return new Action() {
            @Override
            public void run() throws Exception {
                peopleEventsUpdater.updateEvents();
            }
        };
    }

    private Action refreshViews() {
        return new Action() {
            @Override
            public void run() throws Exception {
                viewRefresher.updateAllViews();
            }
        };
    }

    void stopPresenting() {
        monitor.stopObserving();
        disposable.dispose();
    }

}
