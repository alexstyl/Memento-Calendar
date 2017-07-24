package com.alexstyl.specialdates.person;

import android.content.Intent;

import com.alexstyl.specialdates.date.Date;

interface EventPressedListener {
    void onEventPressed(Date date);
    void onContactMethodPressed(Intent intent);
}
