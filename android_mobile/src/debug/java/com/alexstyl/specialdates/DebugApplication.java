package com.alexstyl.specialdates;

import android.content.Context;

import com.alexstyl.specialdates.events.namedays.NamedayModule;

public class DebugApplication extends MementoApplication {

    private DebugAppComponent debugAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        debugAppComponent =
                DaggerDebugAppComponent.builder()
                        .namedayModule(new NamedayModule(this))
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
