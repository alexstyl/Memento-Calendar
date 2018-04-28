package com.alexstyl.specialdates.addevent

import android.content.ContentResolver
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.addevent.operations.ContactOperation
import com.alexstyl.specialdates.addevent.operations.InsertContact
import com.alexstyl.specialdates.addevent.operations.UpdateContact
import com.alexstyl.specialdates.contact.Contact

class AndroidContactOperationsExecutor(
        private val contentResolver: ContentResolver,
        private val tracker: CrashAndErrorTracker) : ContactOperationsExecutor {

    override fun execute(operations: List<ContactOperation>): Boolean {
//        try {
//            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
//            return true
//        } catch (e: RemoteException) {
//            tracker.track(e)
//        } catch (e: OperationApplicationException) {
//            tracker.track(e)
//        }
        return false
    }

    //    private val accountToStoreContact: AccountData
//        get() {
//            val availableAccounts = accountsProvider.availableAccounts
//            return if (availableAccounts.size == 0) {
//                AccountData.NO_ACCOUNT
//            } else {
//                availableAccounts[0]
//            }
//        }

    fun updateExistingContact(contact: Contact): ContactOperations.ContactOperationsBuilder {
//        val rawContactID = rawContactID(contact)
//        val operationsFactory = OperationsFactory(rawContactID, displayStringCreator)
//        val contactEvents = getAllDeviceEventsFor(contact)
//        val operations = operationsFactory.deleteEvents(contactEvents)
        return ContactOperations.ContactOperationsBuilder(arrayListOf(UpdateContact(contact)))
    }

    fun newContact(contactName: String): ContactOperations.ContactOperationsBuilder {
//        val operationsFactory = OperationsFactory.forNewContact(displayStringCreator)
//        val operations = operationsFactory.createContactIn(accountToStoreContact, contactName)
//        return ContactOperationsBuilder(operations)
        return ContactOperations.ContactOperationsBuilder(arrayListOf(InsertContact(contactName)))
    }

    //    private fun rawContactID(contact: Contact): Int {
//        val projection = arrayOf(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID)
//        val selection = ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ?"
//        val selectionArgs = arrayOf(contact.contactID.toString())
//        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null)
//
//        throwIfInvalid(cursor)
//
//        try {
//            if (cursor!!.moveToFirst()) {
//                return cursor.getInt(0)
//            }
//        } finally {
//            cursor!!.close()
//        }
//        return 0
//    }

//    private fun getAllDeviceEventsFor(contact: Contact): List<ContactEvent> {
//        val contactEvents = ArrayList<ContactEvent>()
//        val contactEventsOnDate = peopleEventsProvider.fetchEventsBetween(TimePeriod.aYearFromNow())
//        for (contactEvent in contactEventsOnDate) {
//            val (contactID) = contactEvent.contact
//            if (contactID == contact.contactID && contactEvent.type !== StandardEventType.NAMEDAY) {
//                contactEvents.add(contactEvent)
//            }
//        }
//        return contactEvents
//    }

//    private fun throwIfInvalid(cursor: Cursor?) {
//        if (cursor == null || cursor.isClosed) {
//            throw DeveloperError("Cursor was invalid")
//        }
//    }

}
