package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.ContactsModule;
import com.alexstyl.specialdates.debug.DebugFragment;
import com.alexstyl.specialdates.events.namedays.NamedayModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NamedayModule.class, ContactsModule.class})
public interface DebugAppComponent {
    void inject(DebugFragment fragment);
}
