package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.date.Date;

public interface DateLabelCreator {
    String createLabelWithoutYearFor(Date date);

    String createLabelWithYearFor(Date date);
}
