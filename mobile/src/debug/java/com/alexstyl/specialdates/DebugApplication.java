package com.alexstyl.specialdates;

public class DebugApplication extends MementoApp {

    @Override
    protected void initialiseDependencies() {
        super.initialiseDependencies();
        new OptionalDependencies(this).initialise();
    }
}
