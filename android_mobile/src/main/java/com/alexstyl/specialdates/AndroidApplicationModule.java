package com.alexstyl.specialdates;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.permissions.AndroidPermissions;
import com.alexstyl.specialdates.permissions.MementoPermissions;
import com.evernote.android.job.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AndroidApplicationModule {

    private final Context context;

    AndroidApplicationModule(Context appContext) {
        this.context = appContext;
    }

    @Provides
    Context appContext() {
        return context;
    }

    @Provides
    ContentResolver contentResolver() {
        return context.getContentResolver();
    }

    @Provides
    AppWidgetManager appWidgetManager() {
        return AppWidgetManager.getInstance(context);
    }

    @Provides
    @Singleton
    EventSQLiteOpenHelper sqLiteOpenHelper() {
        return new EventSQLiteOpenHelper(context);
    }

    @Provides
    MementoPermissions permissionChecker(CrashAndErrorTracker tracker) {
        return new AndroidPermissions(tracker, context);
    }

    @Provides
    CrashAndErrorTracker tracker() {
        return new FabricTracker(context);
    }

    @Provides
    @Singleton
    JobManager jobManager(Context context) {
        return JobManager.create(context);
    }
}