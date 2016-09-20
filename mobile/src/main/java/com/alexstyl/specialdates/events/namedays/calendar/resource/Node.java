package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

public interface Node {

    void clear();

    void addDate(String name, DayDate date);

    NameCelebrations getDates(String name);
}
