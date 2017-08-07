package com.alexstyl.specialdates.upcoming;

import android.text.TextUtils;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

public final class NamedaysViewModelFactory {

    private final Date today;

    public NamedaysViewModelFactory(Date today) {
        this.today = today;
    }

    NamedaysViewModel createViewModelFor(NamesInADate namedays) {
        return new NamedaysViewModel(TextUtils.join(", ", namedays.getNames()));
    }
}
