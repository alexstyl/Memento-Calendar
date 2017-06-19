package com.alexstyl.specialdates.events.database;

import android.support.annotation.IntDef;

import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@IntDef({AnnualEventsContract.SOURCE_DEVICE, AnnualEventsContract.SOURCE_FACEBOOK})
public @interface SourceType {
}

