package com.alexstyl.specialdates.contact.actions;

import com.alexstyl.specialdates.R;

public class ContactActionFactory {

    private static ContactActionFactory INSTANCE;

    public static ContactActionFactory get() {
        if (INSTANCE == null) {
            INSTANCE = new ContactActionFactory();
        }
        return INSTANCE;
    }

    public LabeledAction createCallAction(long contactID) {
        CallAction callAction = new CallAction(contactID);
        return new LabeledAction(R.string.action_call, callAction, R.drawable.ic_communication_phone);
    }

    public LabeledAction createSMSAction(long contactID) {
        SMSAction callAction = new SMSAction(contactID);
        return new LabeledAction(R.string.action_text, callAction, R.drawable.ic_communication_chat);
    }

    public LabeledAction createEmailAction(long contactID) {
        EMailAction callAction = new EMailAction(contactID);
        return new LabeledAction(R.string.email_start, callAction, R.drawable.ic_communication_email);
    }
}
