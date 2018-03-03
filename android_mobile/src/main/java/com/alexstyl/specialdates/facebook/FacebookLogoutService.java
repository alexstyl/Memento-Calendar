package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsPersister;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

class FacebookLogoutService {

    private final UpcomingEventsViewRefresher refresher;
    private final FacebookUserSettings preferences;
    private final FacebookFriendsPersister persister;
    private final Scheduler resultScheduler;
    private final OnFacebookLogOutCallback callback;

    private CompositeDisposable disposable = new CompositeDisposable();

    FacebookLogoutService(Scheduler resultScheduler,
                          FacebookUserSettings preferences,
                          FacebookFriendsPersister persister,
                          UpcomingEventsViewRefresher refresher, OnFacebookLogOutCallback callback) {
        this.resultScheduler = resultScheduler;
        this.preferences = preferences;
        this.persister = persister;
        this.refresher = refresher;
        this.callback = callback;
    }

    void logOut() {
        disposable.add(
                Completable.fromAction(clearAllUserPresence())
                        .doOnComplete(onLogOut())
                        .observeOn(resultScheduler)
                        .doOnComplete(refreshAllUI())
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }

    private Action refreshAllUI() {
        return new Action() {
            @Override
            public void run() throws Exception {
                refresher.refreshViews();
            }
        };
    }

    private Action clearAllUserPresence() {
        return new Action() {
            @Override
            public void run() throws Exception {
                preferences.store(UserCredentials.ANNONYMOUS);
                persister.removeAllFriends();
            }
        };
    }

    private Action onLogOut() {
        return new Action() {
            @Override
            public void run() throws Exception {
                callback.onUserLogOut();
            }
        };
    }

    void dispose() {
        disposable.dispose();
    }
}
