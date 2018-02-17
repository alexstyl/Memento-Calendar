package com.alexstyl.specialdates.permissions;

import android.app.Activity;
import android.content.Intent;

public class ContactPermissionRequest {

    static final int CONTACT_REQUEST = 1990;

    private final PermissionNavigator navigator;
    private final PermissionCallbacks callbacks;
    private final AndroidPermissionChecker checker;

    public ContactPermissionRequest(PermissionNavigator navigator, AndroidPermissionChecker checker, PermissionCallbacks callbacks) {
        this.navigator = navigator;
        this.checker = checker;
        this.callbacks = callbacks;
    }

    public boolean permissionIsPresent() {
        return checker.canReadAndWriteContacts();
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

