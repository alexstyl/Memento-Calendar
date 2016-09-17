package com.alexstyl.specialdates.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public final class PermissionChecker {
    private final Context context;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public boolean hasPermission(@MementoPermissions String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
