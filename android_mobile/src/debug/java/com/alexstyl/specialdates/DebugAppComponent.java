package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.ContactsModule;
import com.alexstyl.specialdates.date.DateModule;
import com.alexstyl.specialdates.debug.DebugFragment;
import com.alexstyl.specialdates.donate.DonateModule;
import com.alexstyl.specialdates.events.namedays.NamedayModule;
import com.alexstyl.specialdates.events.peopleevents.DebugModule;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AndroidApplicationModule.class,
        NamedayModule.class,
        ContactsModule.class,
        PeopleEventsModule.class,
        DonateModule.class,
        DebugModule.class,
        DateModule.class})
public interface DebugAppComponent {
    void inject(DebugFragment fragment);
}
