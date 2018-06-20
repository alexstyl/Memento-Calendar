package com.alexstyl.specialdates.events.namedays.activity

import android.app.Activity
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.person.PersonActivity

class NamedaysOnADayNavigator(private val activity: Activity, private val analytics: Analytics) {
    fun toContactDetails(contact: Contact) {
        val intent = PersonActivity.buildIntentFor(activity, contact)
        activity.startActivity(intent)
        analytics.trackNamedaysScreen()
    }

}
