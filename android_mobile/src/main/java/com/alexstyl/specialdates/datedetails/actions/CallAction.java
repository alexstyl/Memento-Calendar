package com.alexstyl.specialdates.datedetails.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.alexstyl.specialdates.entity.Phone;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;

class CallAction implements IntentAction {

    private final long contactID;

    CallAction(long contactID) {
        this.contactID = contactID;
    }

    static boolean isSupported(PackageManager packageManager) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:555"));
        return callIntent.resolveActivity(packageManager) != null;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Phone> phones = (ArrayList<Phone>) ContactUtils.getAllPhones(context.getContentResolver(), contactID);
        if (phones.size() == 1) {
            phones.get(0).dial(context);
        } else {
            ActionDialog dialog = ActionDialog.newPhoneInstance(phones);
            dialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    @Override
    public String getAnalyticsName() {
        return "call";
    }

}
