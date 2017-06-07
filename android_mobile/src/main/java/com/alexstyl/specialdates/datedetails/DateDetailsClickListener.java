package com.alexstyl.specialdates.datedetails;

import android.view.View;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

interface DateDetailsClickListener {

    void onCardClicked(Contact contact);

    void onActionClicked(View v, LabeledAction action);

    void onContactActionsMenuClicked(View v, Contact contact);

    void onNamedayShared(NamesInADate namedays);

    void onSupportCardClicked();
}
