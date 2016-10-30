package com.alexstyl.specialdates.permissions;

import android.app.Activity;
import android.content.Intent;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;

public class PermissionNavigator {

    private final Activity activity;
    private final Analytics analytics;

    public PermissionNavigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
    }

    void toContactPermissionRequired(int requestCode) {
        Intent intent = new Intent(activity, ContactPermissionActivity.class);
        activity.startActivityForResult(intent, requestCode);
        analytics.trackScreen(Screen.CONTACT_PERMISSION_REQUESTED);
    }
}
