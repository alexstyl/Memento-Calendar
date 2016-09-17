package com.alexstyl.specialdates.upcoming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.alexstyl.specialdates.Navigator;

import static android.Manifest.permission.READ_CONTACTS;

public class ContactPermissionRequest {

    public static final int CONTACT_REQUEST = 1990;

    private final Context context;
    private final Navigator navigator;
    private final PermissionCallbacks callbacks;

    public ContactPermissionRequest(Context context, Navigator navigator, PermissionCallbacks callbacks) {
        this.context = context;
        this.navigator = navigator;
        this.callbacks = callbacks;
    }

    public boolean permissionIsPresent() {
        return ActivityCompat.checkSelfPermission(context, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestForPermission() {
        navigator.toContactPermissionRequired(CONTACT_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                callbacks.onPermissionGranted();
            } else {
                callbacks.onPermissionDenied();
            }
        }
    }

    public interface PermissionCallbacks {
        void onPermissionGranted();

        void onPermissionDenied();
    }
}

