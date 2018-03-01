package com.alexstyl.specialdates.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.alexstyl.specialdates.CrashAndErrorTracker;

import static android.Manifest.permission.*;

public class AndroidPermissions implements MementoPermissions {
    private final CrashAndErrorTracker tracker;
    private final Context context;

    public AndroidPermissions(CrashAndErrorTracker tracker, Context context) {
        this.tracker = tracker;
        this.context = context;
    }

    @Override
    public boolean canReadAndWriteContacts() {
        return hasPermission(context, READ_CONTACTS, tracker) && hasPermission(context, WRITE_CONTACTS, tracker);
    }

    @Override
    public boolean canReadExternalStorage() {
        return hasPermission(context, READ_EXTERNAL_STORAGE, tracker);
    }

    private static boolean hasPermission(Context context, String permission, CrashAndErrorTracker tracker) {
        try {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException ex) {
            // some devices randomly throw an exception to this point. just treat as if the permission is not there
            tracker.track(ex);
            return false;
        }
    }
}
