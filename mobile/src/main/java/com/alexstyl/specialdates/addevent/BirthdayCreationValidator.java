package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.date.Date;

public class BirthdayCreationValidator {

    private final Date displayingBirthday;

    public BirthdayCreationValidator() {
        this.displayingBirthday = null;
    }

    public BirthdayCreationValidator(Date displayingBirthday) {
        this.displayingBirthday = displayingBirthday;
    }

    public boolean isBirthdayValid(Date displayingBirthday) {
        return displayingBirthday != null;
    }

    public boolean isContactValid(String displayName) {
        return displayName.length() > 0;
    }

    public boolean isDifferentToInitialBirthday(Date birthday) {
        if (displayingBirthday == null) {
            return birthday != null;
        }
        return !displayingBirthday.equals(birthday);
    }
}
