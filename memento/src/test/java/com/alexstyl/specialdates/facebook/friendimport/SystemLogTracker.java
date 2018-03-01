package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.jetbrains.annotations.NotNull;

class SystemLogTracker implements CrashAndErrorTracker {

    @Override
    public void track(@NotNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void log(@NotNull String message) {
        System.out.println(message);
    }

    @Override
    public void startTracking() {
        // do nothing
    }

    @Override
    public void onNamedayLocaleChanged(@NotNull NamedayLocale locale) {
        // do nothing
    }

    @Override
    public void updateLocaleUsed() {
        // do nothing

    }
}
