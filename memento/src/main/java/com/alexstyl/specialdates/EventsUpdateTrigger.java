package com.alexstyl.specialdates;

public interface EventsUpdateTrigger {

    void startObserving(Callback callback);

    void stopObserving();

    interface Callback {
        void onMonitorTriggered();
    }
}
