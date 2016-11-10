package com.alexstyl.specialdates.addevent;

import android.widget.Button;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactHeroView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;

public class BirthdayFormPresenter {

    private final ContactHeroView contactHeroView;
    private final BirthdayLabelView birthdayLabel;
    private final Button addButton;

    private Contact selectedContact;

    private BirthdayCreationValidator validator;

    public BirthdayFormPresenter(ContactHeroView contactHeroView, BirthdayLabelView birthdayLabel, Button addButton) {
        this.contactHeroView = contactHeroView;
        this.birthdayLabel = birthdayLabel;
        this.addButton = addButton;
        this.validator = new BirthdayCreationValidator();
    }

    public void displayContactAsSelected(final Contact contact) {
        selectedContact = contact;
        contactHeroView.displayAvatarFor(contact);
        // TODO
//        if (contact.hasDateOfBirth()) {
//            birthdayLabel.displayBirthday(contact.getDateOfBirth());
//            addButton.setText(R.string.add_birthday_store_button_update_label);
//            validator = new BirthdayCreationValidator(contact.getDateOfBirth());
//        } else {
        birthdayLabel.displayNoBirthday();
        addButton.setText(R.string.add_birthday_store_button_add_label);
//        }
        updateAddButton();
    }

    void updateAddButton() {
        boolean validData = allDataAreValid();
        addButton.setEnabled(validData);
    }

    private boolean allDataAreValid() {
        return validator.isContactValid(contactHeroView.getText().toString())
                && validator.isBirthdayValid(getDisplayingBirthday())
                && validator.isDifferentToInitialBirthday(getDisplayingBirthday());
    }

    public void presentNoContact() {
        if (isPresentingSomeContact()) {
            validator = new BirthdayCreationValidator();
            selectedContact = null;
            contactHeroView.hideAvatar();
            addButton.setText(R.string.add_birthday_store_button_add_label);
        }
        updateAddButton();
    }

    private boolean isPresentingSomeContact() {
        return selectedContact != null;
    }

    public void presentBirthday(Date birthday) {
        birthdayLabel.displayBirthday(birthday);
        updateAddButton();
    }

    public boolean isPresentingNoStoredContact() {
        return selectedContact == null;
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }

    public Date getDisplayingBirthday() {
        return birthdayLabel.getDisplayingBirthday();
    }
}
