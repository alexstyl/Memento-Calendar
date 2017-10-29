package com.alexstyl.specialdates;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.permissions.PermissionChecker;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    private final Context context;

    AppModule(Context appContext) {
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
    SQLiteOpenHelper sqLiteOpenHelper() {
        return new EventSQLiteOpenHelper(context);
    }

    @Provides
    PermissionChecker permissionChecker() {
        return new PermissionChecker(context);
    }
}
