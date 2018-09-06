package com.alexstyl.specialdates.events.peopleevents

import android.content.ContentValues

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract

class ContactEventsMarshaller(private val dateLabelCreator: ShortDateLabelCreator) {

    fun marshall(item: List<ContactEvent>): List<ContentValues> {
        return item.map { event -> createValuesFor(event) }.toList()
    }

    private fun createValuesFor(event: ContactEvent): ContentValues {
        val contact = event.contact

        val values = ContentValues(DEFAULT_VALUES_SIZE)

        values.put(AnnualEventsContract.CONTACT_ID, contact.contactID)
        values.put(AnnualEventsContract.DISPLAY_NAME, contact.displayName.toString())
        values.put(AnnualEventsContract.DATE, dateLabelCreator.createLabelWithYearPreferredFor(event.date))
        values.put(AnnualEventsContract.EVENT_TYPE, event.type.id)
        values.put(AnnualEventsContract.SOURCE, contact.source)
        values.put(AnnualEventsContract.VISIBLE, IS_VISIBILE)

        values.put(AnnualEventsContract.DEVICE_EVENT_ID, event.deviceEventId ?: -1)

        return values
    }

    companion object {

        private const val DEFAULT_VALUES_SIZE = 5
        private const val IS_VISIBILE = 1
    }

}
