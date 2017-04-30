package com.alexstyl.specialdates.donate;

import java.util.ArrayList;
import java.util.List;

public final class DonateMonitor {

    private static List<DonateMonitorListener> listeners = new ArrayList<>();

    private static final DonateMonitor INSTANCE = new DonateMonitor();

    public static DonateMonitor getInstance() {
        return INSTANCE;
    }

    void onDonationPlaced() {
        for (DonateMonitorListener listener : listeners) {
            listener.onUserDonated();
        }
    }

    public void addListener(DonateMonitorListener l) {
        listeners.add(l);
    }

    public void removeListener(DonateMonitorListener l) {
        listeners.remove(l);
    }

    public interface DonateMonitorListener {
        void onUserDonated();
    }
}
