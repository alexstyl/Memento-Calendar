package com.alexstyl.specialdates.contact.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.widget.Toast;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.entity.Email;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;

/**
 * LabeledAction that displays a dialog wih
 * <p>Created by Alex on 9/5/2014.</p>
 */
public class EMailAction implements IntentAction {

    private final long contactId;

    EMailAction(long contactId) {
        this.contactId = contactId;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Email> emails = (ArrayList<Email>) ContactUtils.getAllEMails(context.getContentResolver(), contactId);
        if (emails.size() == 1) {
            try {
                emails.get(0).sendMail(context);
            } catch (Exception e) {
                ErrorTracker.track(e);
                Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            ActionDialog dialog = ActionDialog.newEmailInstance(emails);
            dialog.show(activity.getSupportFragmentManager(), null);
        }

    }

}
