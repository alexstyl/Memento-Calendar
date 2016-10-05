package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.List;

public interface SpecialNamedays {

    NamesInADate getNamedayOn(Date date);

    NameCelebrations getNamedaysFor(String name, int year);

    List<String> getAllNames();
}
