package com.alexstyl.specialdates.addevent

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.OperationApplicationException
import android.database.Cursor
import android.os.RemoteException
import android.provider.ContactsContract
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.addevent.operations.ContactOperation
import com.alexstyl.specialdates.addevent.operations.InsertContact
import com.alexstyl.specialdates.addevent.operations.UpdateContact
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.novoda.notils.exception.DeveloperError
import java.util.ArrayList

class AndroidContactOperationsExecutor(
        private val contentResolver: ContentResolver,
        private val tracker: CrashAndErrorTracker,
        private val displayStringCreator: ShortDateLabelCreator,
        private val peopleEventsProvider: PeopleEventsProvider,
        private val accountsProvider: WriteableAccountsProvider)
    : ContactOperationsExecutor {

    override fun execute(operations: List<ContactOperation>): Boolean {
        val operationsFactory = makeFactoryFor(operations[0])

        try {
            val contentProviderOperations = ArrayList(operations.fold(emptyList<ContentProviderOperation>(), { list, contactOperation ->
                list + operationsFactory.createOperationsFor(contactOperation)
            }))
            contentResolver.applyBatch(ContactsContract.AUTHORITY, contentProviderOperations)
            return true
        } catch (e: RemoteException) {
            tracker.track(e)
        } catch (e: OperationApplicationException) {
            tracker.track(e)
        }
        return false
    }

    private fun makeFactoryFor(contactOperation: ContactOperation): OperationsFactory {
        if (contactOperation is InsertContact) {
            return OperationsFactory.forNewContact(displayStringCreator, peopleEventsProvider, accountsProvider)
        } else if (contactOperation is UpdateContact) {
            val rawContactID = rawContactID(contactOperation.contact)
            return OperationsFactory(rawContactID, displayStringCreator, peopleEventsProvider, accountsProvider)
        }
        throw IllegalArgumentException("Cannot make factory for $contactOperation")
    }


    private fun rawContactID(contact: Contact): Int {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID)
        val selection = ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ?"
        val selectionArgs = arrayOf(contact.contactID.toString())
        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null)

        throwIfInvalid(cursor)

        try {
            if (cursor!!.moveToFirst()) {
                return cursor.getInt(0)
            }
        } finally {
            cursor!!.close()
        }
        return 0
    }

    private fun throwIfInvalid(cursor: Cursor?) {
        if (cursor == null || cursor.isClosed) {
            throw DeveloperError("Cursor was invalid")
        }
    }
}
