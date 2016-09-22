package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;

public interface SpecialNamedays {

    NamesInADate getNamedayByDate(DayDate date);

    ArrayList<String> getAllNames();

    NameCelebrations getNamedaysFor(String name, int year);
}
