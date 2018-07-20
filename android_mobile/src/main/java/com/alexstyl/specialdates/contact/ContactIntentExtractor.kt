package com.alexstyl.specialdates.contact

import android.content.Intent
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoConstants


class ContactIntentExtractor(val tracker: CrashAndErrorTracker,
                             val contactsProvider: ContactsProvider) {

    companion object {
        val EXTRA_CONTACT_SOURCE = "${MementoConstants.PACKAGE}.extra:source"
        val EXTRA_CONTACT_ID = "${MementoConstants.PACKAGE}.extra:contactId"
    }

    fun getContactExtra(intent: Intent): Contact? {
        val contactID = intent.getLongExtra(EXTRA_CONTACT_ID, -1)
        if (contactID == -1L) {
            return null
        }
        @ContactSource val contactSource = intent.getIntExtra(EXTRA_CONTACT_SOURCE, -1)
        return if (contactSource == -1) {
            return null
        } else contactFor(contactID, contactSource)
    }


    private fun contactFor(contactID: Long, contactSource: Int): Contact? {
        return try {
            contactsProvider.getContact(contactID, contactSource)
        } catch (e: ContactNotFoundException) {
            tracker.track(e)
            null
        }
    }
}


