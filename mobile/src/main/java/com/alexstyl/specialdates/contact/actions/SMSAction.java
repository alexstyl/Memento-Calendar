package com.alexstyl.specialdates.contact.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.widget.Toast;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.entity.Phone;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;

/**
 * LabeledAction that displays a dialog wih
 * <p>Created by Alex on 9/5/2014.</p>
 */
public class SMSAction implements IntentAction {

    private final long contactId;

    SMSAction(long contactId) {
        this.contactId = contactId;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Phone> phones = (ArrayList<Phone>) ContactUtils.getAllPhones(context.getContentResolver(), contactId);
        if (phones.size() == 1) {
            try {
                phones.get(0).sendText(context);
            } catch (Exception e) {
                ErrorTracker.track(e);
                Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            ActionDialog dialog = ActionDialog.newSMSInstance(phones);
            dialog.show(activity.getSupportFragmentManager(), null);
        }

    }

}
