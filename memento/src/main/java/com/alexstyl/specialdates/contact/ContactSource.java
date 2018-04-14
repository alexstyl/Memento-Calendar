package com.alexstyl.specialdates.contact;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

@Retention(RetentionPolicy.CLASS)
@IntDef({SOURCE_DEVICE, SOURCE_FACEBOOK})
public @interface
ContactSource {
    int SOURCE_DEVICE = 1;
    int SOURCE_FACEBOOK = 2;
}

