package com.alexstyl.specialdates.datedetails;

import android.app.Activity;
import android.content.Intent;

import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.person.PersonActivity;

class DateDetailsNavigator {
    private final Activity activity;
    private final Analytics analytics;
    private final ExternalNavigator externalNavigator;

    DateDetailsNavigator(Activity activity, Analytics analytics, ExternalNavigator externalNavigator) {
        this.activity = activity;
        this.analytics = analytics;
        this.externalNavigator = externalNavigator;
    }


    void toContactDetails(Contact contact) {
        Intent intent = PersonActivity.buildIntentFor(activity, contact);
        activity.startActivity(intent);
        analytics.trackContactDetailsViewed(contact);
    }

    void toPlayStore() {
        externalNavigator.toPlayStore();
    }
}
