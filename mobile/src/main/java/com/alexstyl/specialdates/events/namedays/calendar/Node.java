package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

public interface Node {

    void clear();

    void addDate(String name, DayDate date);

    NameCelebrations getDates(String name);
}
