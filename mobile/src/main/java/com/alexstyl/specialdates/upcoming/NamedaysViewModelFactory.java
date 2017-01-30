package com.alexstyl.specialdates.upcoming;

import android.text.TextUtils;
import android.view.View;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

final class NamedaysViewModelFactory {

    private final Date today;

    NamedaysViewModelFactory(Date today) {
        this.today = today;
    }

    NamedaysViewModel createViewModelFor(Date date, NamesInADate namedays) {
        NamedaysViewModel namedaysViewModel;
        if (namedays == null) {
            namedaysViewModel = new NamedaysViewModel("", View.GONE, 0);
        } else {
            int maxLines = date.equals(today) ? 3 : 1;
            namedaysViewModel = new NamedaysViewModel(TextUtils.join(", ", namedays.getNames()), View.VISIBLE, maxLines);
        }
        return namedaysViewModel;
    }
}
