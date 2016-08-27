package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.contact.Birthday;

public class BirthdayCreationValidator {

    private final Birthday displayingBirthday;

    public BirthdayCreationValidator() {
        this.displayingBirthday = null;
    }

    public BirthdayCreationValidator(Birthday displayingBirthday) {
        this.displayingBirthday = displayingBirthday;
    }

    public boolean isBirthdayValid(Birthday displayingBirthday) {
        return displayingBirthday != null;
    }

    public boolean isContactValid(String displayName) {
        return displayName.length() > 0;
    }

    public boolean isDifferentToInitialBirthday(Birthday birthday) {
        if (displayingBirthday == null) {
            return birthday != null;
        }
        return !displayingBirthday.equals(birthday);
    }
}
