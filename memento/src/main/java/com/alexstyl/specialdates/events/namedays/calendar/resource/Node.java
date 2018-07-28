package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

public interface Node {

    void addDate(String name, Date date);

    NameCelebrations getDates(String name);
}
