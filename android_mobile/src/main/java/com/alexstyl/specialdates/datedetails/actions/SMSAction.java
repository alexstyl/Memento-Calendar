package com.alexstyl.specialdates.datedetails.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Telephony;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.entity.Phone;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;

/**
 * LabeledAction that displays a dialog wih
 * <p>Created by Alex on 9/5/2014.</p>
 */
class SMSAction implements IntentAction {

    private final long contactId;

    SMSAction(long contactId) {
        this.contactId = contactId;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Phone> phones = (ArrayList<Phone>) ContactUtils.getAllPhones(context.getContentResolver(), contactId);
        if (phones.size() == 1) {
            phones.get(0).sendText(context);
        } else {
            ActionDialog dialog = ActionDialog.newSMSInstance(phones);
            dialog.show(activity.getSupportFragmentManager(), null);
        }

    }

    @Override
    public String getName() {
        return "SMS";
    }

    static boolean isSupported(Context context, PackageManager packageManager) {
        Intent smsIntent = createSMSIntent(context);
        return smsIntent.resolveActivity(packageManager) != null;
    }

    private static Intent createSMSIntent(Context context) {
        if (Version.hasKitKat()) {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:555"));
            if (defaultSmsPackageName != null) {
                smsIntent.setPackage(defaultSmsPackageName);
            }
            return smsIntent;
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.fromParts("sms", "555", null));
            return smsIntent;
        }
    }
}
