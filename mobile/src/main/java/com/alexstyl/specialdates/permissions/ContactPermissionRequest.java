package com.alexstyl.specialdates.permissions;

import android.app.Activity;
import android.content.Intent;

import com.alexstyl.specialdates.Navigator;

import static android.Manifest.permission.READ_CONTACTS;

public class ContactPermissionRequest {

    public static final int CONTACT_REQUEST = 1990;

    private final Navigator navigator;
    private final PermissionCallbacks callbacks;
    private final PermissionChecker checker;

    public ContactPermissionRequest(Navigator navigator, PermissionChecker checker, PermissionCallbacks callbacks) {
        this.navigator = navigator;
        this.checker = checker;
        this.callbacks = callbacks;
    }

    public boolean permissionIsPresent() {
        return checker.hasPermission(READ_CONTACTS);
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

