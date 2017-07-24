package com.alexstyl.specialdates.person;

import com.alexstyl.specialdates.date.Date;

interface EventPressedListener {
    void onEventPressed(Date date);
    void onContactActionPressed(ContactActionViewModel intent);
}
