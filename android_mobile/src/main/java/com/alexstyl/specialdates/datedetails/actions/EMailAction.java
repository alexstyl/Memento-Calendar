package com.alexstyl.specialdates.datedetails.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.alexstyl.specialdates.entity.Email;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;

class EMailAction implements IntentAction {

    private final long contactId;

    EMailAction(long contactId) {
        this.contactId = contactId;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Email> emails = (ArrayList<Email>) ContactUtils.getAllEMails(context.getContentResolver(), contactId);
        if (emails.size() == 1) {
            emails.get(0).sendMail(context);
        } else {
            ActionDialog dialog = ActionDialog.newEmailInstance(emails);
            dialog.show(activity.getSupportFragmentManager(), null);
        }

    }

    @Override
    public String getName() {
        return "email";
    }

    static boolean isSupported(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "email@gmail.com", null));
        return intent.resolveActivity(packageManager) != null;
    }

}
