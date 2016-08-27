package com.alexstyl.specialdates.datedetails;

import android.view.View;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;

public interface ContactCardListener {

    void onCardClicked(View v, Contact contact);

    void onActionClicked(View v, LabeledAction action);

    void onContactActionsMenuClicked(View v, Contact contact);

}
