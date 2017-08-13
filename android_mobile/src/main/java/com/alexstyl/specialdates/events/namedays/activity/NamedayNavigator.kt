package com.alexstyl.specialdates.events.namedays.activity

import android.app.Activity
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.person.PersonActivity

class NamedayNavigator(private val activity: Activity) {
    fun toContactDetails(contact: Contact) {
        val intent = PersonActivity.buildIntentFor(activity, contact)
        activity.startActivity(intent)
    }

}
