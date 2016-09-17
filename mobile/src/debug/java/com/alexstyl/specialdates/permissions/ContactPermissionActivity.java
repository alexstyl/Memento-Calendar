package com.alexstyl.specialdates.permissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.EventType;
import com.alexstyl.specialdates.ui.base.ThemedActivity;

@TargetApi(Build.VERSION_CODES.M)
public class ContactPermissionActivity extends ThemedActivity {

    private static final int REQUEST_CONTACT_PERMISSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_permission_request);
        setResult(RESULT_CANCELED);
    }

    private void requestContactPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
