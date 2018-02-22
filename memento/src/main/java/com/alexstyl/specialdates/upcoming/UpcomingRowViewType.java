package com.alexstyl.specialdates.upcoming;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.*;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        DATE_HEADER,
        BANKHOLIDAY,
        NAMEDAY_CARD,
        CONTACT_EVENT
})
public @interface UpcomingRowViewType {
    int DATE_HEADER = 0;
    int BANKHOLIDAY = 1;
    int NAMEDAY_CARD = 2;
    int CONTACT_EVENT = 3;
}
