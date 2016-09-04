package com.alexstyl.specialdates.events.namedays.calendar;

import java.util.ArrayList;
import java.util.List;

public class EasternNameday {

    private int date;
    private final List<String> celebratingNames;

    public EasternNameday(int daysToEaster,
                          List<String> celebratingNames) {
        this.date = daysToEaster;
        this.celebratingNames = celebratingNames;
    }

    public List<String> getNamesCelebrating() {
        return new ArrayList<>(celebratingNames);
    }

    public int getDateToEaster() {
        return date;
    }
}
