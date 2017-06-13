package com.alexstyl.specialdates.contact.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.widget.Toast;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.entity.Phone;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.dialog.ActionDialog;
import com.alexstyl.specialdates.util.ContactUtils;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;

/**
 * LabeledAction that displays a dialog wih
 * <p>Created by Alex on 9/5/2014.</p>
 */
public class CallAction implements IntentAction {

    private final long contactID;

    CallAction(long contactID) {
        this.contactID = contactID;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        MementoActivity activity = (MementoActivity) context;
        ArrayList<Phone> phones = (ArrayList<Phone>) ContactUtils.getAllPhones(context.getContentResolver(), contactID);
        if (phones.size() == 1) {
            try {
                phones.get(0).dial(context);
            } catch (Exception e) {
                Log.d(e);
                Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            ActionDialog dialog = ActionDialog.newPhoneInstance(phones);
            dialog.show(activity.getSupportFragmentManager(), null);
        }

    }

    @Override
    public String getName() {
        return "call";
    }

}
