package com.alexstyl.specialdates;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

class OptionalDependencies {

    private final Context context;

    OptionalDependencies(Context context) {
        this.context = context.getApplicationContext();
    }

    void initialise() {
        Stetho.initializeWithDefaults(context);
//        if (!LeakCanary.isInAnalyzerProcess(context)) {
//            LeakCanary.install((Application) context);
//        }
    }
}
