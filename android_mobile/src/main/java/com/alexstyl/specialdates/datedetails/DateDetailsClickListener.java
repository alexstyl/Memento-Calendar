package com.alexstyl.specialdates.datedetails;

import android.view.View;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.List;

interface DateDetailsClickListener {

    void onCardClicked(Contact contact);

    void onActionClicked(LabeledAction action);

    void onContactActionsMenuClicked(View v, Contact contact, List<LabeledAction> actions);

    void onNamedayShared(NamesInADate namedays);

    void onSupportCardClicked();
}
