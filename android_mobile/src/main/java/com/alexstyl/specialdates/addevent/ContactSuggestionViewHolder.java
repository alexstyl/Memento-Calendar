package com.alexstyl.specialdates.addevent;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.contact.Contact;
import com.novoda.notils.text.SimpleTextWatcher;

final class ContactSuggestionViewHolder extends RecyclerView.ViewHolder {

    private ContactSuggestionView contactSuggestionView;

    ContactSuggestionViewHolder(ContactSuggestionView contactSuggestionView) {
        super(contactSuggestionView);
        this.contactSuggestionView = contactSuggestionView;
    }

    public void bind(final ContactDetailsListener listener) {
        contactSuggestionView.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(Contact contact) {
                listener.onContactSelected(contact);
            }

            @Override
            public void onContactCleared() {
                listener.onContactCleared();
            }
        });
        contactSuggestionView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                listener.onNameModified(text.toString());
            }
        });

    }
}
