package com.alexstyl.specialdates.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import static android.Manifest.permission.READ_CONTACTS;

public class PermissionChecker {
    private final Context context;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public boolean canReadAndWriteContacts() {
        return ActivityCompat.checkSelfPermission(context, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}
