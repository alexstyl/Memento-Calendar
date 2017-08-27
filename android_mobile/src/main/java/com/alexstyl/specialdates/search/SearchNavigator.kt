package com.alexstyl.specialdates.search

import android.app.Activity
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity
import com.alexstyl.specialdates.person.PersonActivity

internal class SearchNavigator(private val activity: Activity, private val analytics: Analytics) {

    fun toContactDetails(contact: Contact) {
        val intent = PersonActivity.buildIntentFor(activity, contact)
        activity.startActivity(intent)
        analytics.trackContactDetailsViewed(contact)
    }

    fun toNamedays(date: Date) {
        val currentYearDate = Date.on(date.dayOfMonth, date.month, Date.CURRENT_YEAR)
        val intent = NamedayActivity.getStartIntent(activity, currentYearDate)
        activity.startActivity(intent)
    }
}
