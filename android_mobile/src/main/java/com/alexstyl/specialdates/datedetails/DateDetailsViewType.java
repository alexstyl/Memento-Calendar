package com.alexstyl.specialdates.datedetails;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.datedetails.DateDetailsViewType.*;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        NAMEDAY,
        CONTACT_EVENT,
        RATE_APP,
        BANKHOLIDAY
})
@interface DateDetailsViewType {
    int CONTACT_EVENT = 0;
    int NAMEDAY = 1;
    int BANKHOLIDAY = 2;
    int RATE_APP = 3;
}



