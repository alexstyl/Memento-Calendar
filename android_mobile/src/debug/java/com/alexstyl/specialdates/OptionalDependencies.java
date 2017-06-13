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

        if (LeakCanary.isInAnalyzerProcess(context)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
        } else {
            LeakCanary.install((Application) context);
        }
    }
}
