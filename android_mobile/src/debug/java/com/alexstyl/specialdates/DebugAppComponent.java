package com.alexstyl.specialdates;

import com.alexstyl.specialdates.debug.DebugFragment;
import com.alexstyl.specialdates.events.namedays.NamedayModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NamedayModule.class})
public interface DebugAppComponent {

    void inject(DebugFragment fragment);
}
