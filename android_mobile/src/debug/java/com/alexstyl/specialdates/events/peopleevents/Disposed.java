package com.alexstyl.specialdates.events.peopleevents;

import io.reactivex.disposables.Disposable;

class Disposed implements Disposable {
    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return true;
    }
}
