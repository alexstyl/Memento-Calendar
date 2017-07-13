package com.alexstyl.specialdates.datedetails.actions;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactActionFactory {

    private final ContentResolver resolver;
    private final PackageManager packageManager;
    private final Context context;

    public ContactActionFactory(Context context, ContentResolver resolver, PackageManager packageManager) {
        this.context = context;
        this.resolver = resolver;
        this.packageManager = packageManager;
    }

    public List<LabeledAction> createActionsFor(Contact contact) {
        List<LabeledAction> actions = new ArrayList<>(2);
        int contactSource = contact.getSource();
        if (contactSource == ContactSource.SOURCE_FACEBOOK) {
            return createFacebookActionsFor(contact);
        } else if (contactSource == ContactSource.SOURCE_DEVICE) {
            long contactId = contact.getContactID();
            if (ContactUtils.hasPhoneNumber(resolver, contactId)) {
                addCallActionIfSupported(actions, contactId);
                createSMSActionIfSupported(actions, contactId);
            }

            boolean hasEmails = ContactUtils.hasEmail(resolver, contactId);
            if (hasEmails) {
                addEmailActionIfSupported(actions, contactId);
            }
            return actions;
        } else {
            throw new IllegalStateException("Unknown contact source " + contactSource);
        }
    }

    private List<LabeledAction> createFacebookActionsFor(Contact contact) {
        List<LabeledAction> actions = new ArrayList<>(1);
        actions.add(new LabeledAction(R.string.facebook_send_message, new FacebookMessengerAction(contact.getContactID()), R.drawable.ic_communication_chat));
        return actions;
    }

    private void addCallActionIfSupported(List<LabeledAction> actions, long contactID) {
        if (CallAction.isSupported(packageManager)) {
            CallAction callAction = new CallAction(contactID);
            actions.add(new LabeledAction(R.string.action_call, callAction, R.drawable.ic_communication_phone));
        }
    }

    private void createSMSActionIfSupported(List<LabeledAction> actions, long contactID) {
        if (SMSAction.isSupported(context, packageManager)) {
            SMSAction smsAction = new SMSAction(contactID);
            actions.add(new LabeledAction(R.string.action_text, smsAction, R.drawable.ic_communication_chat));
        }
    }

    private void addEmailActionIfSupported(List<LabeledAction> actions, long contactID) {
        if (EMailAction.isSupported(packageManager)) {
            EMailAction eMailAction = new EMailAction(contactID);
            actions.add(new LabeledAction(R.string.email_start, eMailAction, R.drawable.ic_communication_email));
        }
    }
}
