package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

import java.util.ArrayList;
import java.util.List;

class SettingsMonitor implements EventUpdatedMonitor {

    private List<EventUpdatedMonitor> monitors;

    public static SettingsMonitor newInstance(Context context) {
        ArrayList<EventUpdatedMonitor> monitors = new ArrayList<>(2);
        monitors.add(new NamedaySettingsMonitor(NamedayPreferences.newInstance(context)));
        monitors.add(new BankHolidaysMonitor(BankHolidaysPreferences.newInstance(context)));
        return new SettingsMonitor(monitors);
    }

    private SettingsMonitor(List<EventUpdatedMonitor> monitors) {
        this.monitors = monitors;
    }

    @Override
    public void initialise() {
        for (EventUpdatedMonitor monitor : monitors) {
            monitor.initialise();
        }
    }

    @Override
    public boolean dataWasUpdated() {
        for (EventUpdatedMonitor monitor : monitors) {
            if (monitor.dataWasUpdated()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void refreshData() {
        for (EventUpdatedMonitor monitor : monitors) {
            monitor.refreshData();
        }
    }
}
