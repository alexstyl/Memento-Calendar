package com.alexstyl.specialdates.upcoming.widget.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.getDateExtraOrThrow
import com.alexstyl.specialdates.date.putExtraDate
import com.alexstyl.specialdates.events.namedays.activity.NamedaysOnADayActivity
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.person.PersonActivity
import javax.inject.Inject

/**
 * Activity with the sole purpose to forward intent launches to other activities from clicks to widgets.
 * This activity has no UI
 */
class WidgetRouterActivity : Activity() {

    @Inject
    lateinit var contactsProvider: ContactsProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        val startIntent = routeIntent(intent, this)
        startActivity(startIntent)
        finish()
    }

    fun routeIntent(intent: Intent, context: Context): Intent = when (intent.action) {
        ACTION_VIEW_CONTACT -> PersonActivity.buildIntentFor(context, intent.contact())
        ACTION_VIEW_NAMEDAY -> NamedaysOnADayActivity.getStartIntent(context, intent.getDateExtraOrThrow())
        else -> {
            HomeActivity.getStartIntent(this)
        }
    }

    private fun Intent.contact(): Contact {
        val contactId = getLongExtra(EXTRA_CONTACT_ID, -1)
        val source = getIntExtra(EXTRA_CONTACT_SOURCE, -1)
        return contactsProvider.getContact(contactId, source)
    }

    companion object {

        private const val ACTION_VIEW_NAMEDAY = "action:view_nameday"
        private const val ACTION_VIEW_CONTACT = "action:view_contact"
        private const val EXTRA_CONTACT_ID = "extra:contact_id"
        private const val EXTRA_CONTACT_SOURCE = "extra:contact_source"

        fun buildIntent(context: Context): Intent = Intent(context, WidgetRouterActivity::class.java)

        fun buildContactIntent(context: Context, contact: Contact): Intent =
                Intent(context, WidgetRouterActivity::class.java)
                        .apply {
                            action = ACTION_VIEW_CONTACT
                            putExtra(EXTRA_CONTACT_ID, contact.contactID)
                            putExtra(EXTRA_CONTACT_SOURCE, contact.source)
                        }


        fun buildNamedayIntent(date: Date, context: Context): Intent =

                Intent(context, WidgetRouterActivity::class.java)
                        .apply {
                            action = ACTION_VIEW_NAMEDAY
                            putExtraDate(date)
                        }
    }
}


