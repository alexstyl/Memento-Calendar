package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.Monitor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

class PeopleEventsPresenter {

    private final PeopleEventsUpdater peopleEventsUpdater;
    private final Scheduler resultScheduler;
    private final EventsRefreshRequestsMonitor monitor;
    private final PeopleEventsViewRefresher viewRefresher;

    private Disposable subscribe = new Disposed();

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
        subscribe =
                Observable.create(updateEvents())
                        .doOnComplete(refreshViews())
                        .observeOn(resultScheduler)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
    }

    private ObservableOnSubscribe<Object> updateEvents() {
        return new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                peopleEventsUpdater.updateEvents();
                emitter.onComplete();
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
        subscribe.dispose();
    }

}
