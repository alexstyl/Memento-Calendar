package com.alexstyl.specialdates.datedetails;

import com.alexstyl.specialdates.events.PeopleEventsContract;

public class PeopleEventsQuery {

    public class Date {

        private static final String BIRTHDAYS_ONLY_AND = PeopleEventsContract.PeopleEvents.EVENT_TYPE + " = " + PeopleEventsContract.PeopleEvents.TYPE_BIRTHDAY + " AND ";

        public static final String SELECT = PeopleEventsContract.PeopleEvents.DATE + " = ?";
        public static final String SELECT_ONLY_BIRTHDAYS = BIRTHDAYS_ONLY_AND + SELECT;

    }
}
