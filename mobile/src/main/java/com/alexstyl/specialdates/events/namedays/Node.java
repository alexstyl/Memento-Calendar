package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

public interface Node {

    void clear();

    void addDate(String name, DayDate date);

    NameCelebrations getDates(String name);
}
