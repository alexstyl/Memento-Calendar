package com.alexstyl.specialdates;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class OptionalDependencies {

    private final Context context;

    public OptionalDependencies(Context context) {
        this.context = context.getApplicationContext();
    }

    void initialise() {
        Stetho.initializeWithDefaults(context);
        LeakCanary.install((Application) context);
    }
}
