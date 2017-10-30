package com.alexstyl.specialdates;

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;

public class DebugApplication extends MementoApplication {

    private DebugAppComponent debugAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        debugAppComponent =
                DaggerDebugAppComponent.builder()
                        .appModule(new AppModule(this))
                        .peopleEventsModule(new PeopleEventsModule(this))
                        .build();
    }

    @Override
    protected void initialiseDependencies() {
        super.initialiseDependencies();
        new OptionalDependencies(this).initialise();
    }

    public DebugAppComponent getDebugAppComponent() {
        return debugAppComponent;
    }

}
