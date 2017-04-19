package com.alexstyl.specialdates.permissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.alexstyl.specialdates.ExternalWidgetRefresher;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.novoda.notils.caster.Views;

@TargetApi(Build.VERSION_CODES.M) // Runtime permissions were added in M (SDK 23)
public class ContactPermissionActivity extends ThemedActivity {

    private static final int REQUEST_CONTACT_PERMISSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_permission_request);
        setResult(RESULT_CANCELED);

        Button grantButton = Views.findById(this, R.id.contact_permission_grant_button);
        grantButton.setOnClickListener(onGrantPermissionButtonClicked);
    }

    private final View.OnClickListener onGrantPermissionButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestContactPermission();
        }
    };

    private void requestContactPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setResult(RESULT_OK);
            finish();
            ExternalWidgetRefresher.get(getApplicationContext()).refreshAllWidgets();
        }
    }
}
