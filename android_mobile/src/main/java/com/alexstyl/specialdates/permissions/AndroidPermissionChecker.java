package com.alexstyl.specialdates.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.alexstyl.specialdates.ErrorTracker;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AndroidPermissionChecker implements MementoPermissionsChecker {
    private final Context context;

    public AndroidPermissionChecker(Context context) {
        this.context = context;
    }

    @Override
    public boolean canReadAndWriteContacts() {
        return hasPermission(context, READ_CONTACTS) && hasPermission(context, WRITE_CONTACTS);
    }

    @Override
    public boolean canReadExternalStorage() {
        return hasPermission(context, READ_EXTERNAL_STORAGE);
    }

    private static boolean hasPermission(Context context, String permission) {
        try {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException ex) {
            // some devices randomly throw an exception to this point. just treat as if the permission is not there
            ErrorTracker.track(ex);
            return false;
        }
    }
}
