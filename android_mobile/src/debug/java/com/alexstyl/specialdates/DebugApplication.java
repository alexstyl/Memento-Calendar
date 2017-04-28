package com.alexstyl.specialdates;

public class DebugApplication extends MementoApplication {

    @Override
    protected void initialiseDependencies() {
        super.initialiseDependencies();
        new OptionalDependencies(this).initialise();
    }
}
