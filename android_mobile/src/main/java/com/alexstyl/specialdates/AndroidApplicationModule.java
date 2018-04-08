package com.alexstyl.specialdates;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.alexstyl.Logger;
import com.alexstyl.android.AndroidLogger;
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
    Resources resources() {
        return context.getResources();
    }

    @Provides
    PackageManager pkgManager() {
        return context.getPackageManager();
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
    JobManager jobManager() {
        return JobManager.create(context);
    }

    @Provides
    NotificationManager notificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    Logger logger() {
        return new AndroidLogger();
    }
}
