package com.alexstyl.specialdates.search

import android.app.Activity
import android.content.Intent
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.activity.NamedaysOnADayActivity
import com.alexstyl.specialdates.permissions.ContactPermissionActivity
import com.alexstyl.specialdates.person.PersonActivity

class SearchNavigator(private val analytics: Analytics) {

    fun toContactDetails(contact: Contact, activity: Activity) {
        val intent = PersonActivity.buildIntentFor(activity, contact)
        activity.startActivity(intent)
        analytics.trackContactDetailsViewed(contact)
    }

    fun toNamedays(date: Date, activity: Activity) {
        val currentYearDate = dateOn(date.dayOfMonth, date.month, Date.CURRENT_YEAR)
        val intent = NamedaysOnADayActivity.getStartIntent(activity, currentYearDate)
        activity.startActivity(intent)
    }

    fun toContactPermission(activity: Activity) {
        val intent = Intent(activity, ContactPermissionActivity::class.java)
        analytics.trackScreen(Screen.CONTACT_PERMISSION_REQUESTED)
        activity.startActivity(intent)
    }
}
