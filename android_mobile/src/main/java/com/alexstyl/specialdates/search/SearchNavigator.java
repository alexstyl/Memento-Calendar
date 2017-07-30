package com.alexstyl.specialdates.search;

import android.app.Activity;
import android.content.Intent;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.person.PersonActivity;

class SearchNavigator {
    private final Activity activity;
    private final Analytics analytics;

    SearchNavigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
    }

    void toContactDetails(Contact contact) {
        Intent intent = PersonActivity.buildIntentFor(activity, contact);
        activity.startActivity(intent);
        analytics.trackContactDetailsViewed(contact);
    }
}
