package com.alexstyl.specialdates;

public interface Monitor {

    void startObserving(Callback callback);

    void stopObserving();

    interface Callback {
        void onMonitorTriggered();
    }
}
