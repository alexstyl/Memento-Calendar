package com.alexstyl.specialdates;

import android.content.Context;

public class DebugApplication extends MementoApplication {

    private DebugAppComponent debugAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        debugAppComponent =
                DaggerDebugAppComponent.builder()
                        .appModule(new AppModule(this))
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
