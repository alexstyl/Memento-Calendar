package com.alexstyl.specialdates;

import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;
import com.alexstyl.specialdates.images.ImageModule;

public class DebugApplication extends MementoApplication {

    private DebugAppComponent debugAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        debugAppComponent =
                DaggerDebugAppComponent.builder()
                        .androidApplicationModule(new AndroidApplicationModule(this))
                        .peopleEventsModule(new PeopleEventsModule(this))
                        .imageModule(new ImageModule(getResources()))
                        .resourcesModule(new ResourcesModule(this, getResources()))
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
