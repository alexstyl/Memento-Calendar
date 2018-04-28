package com.alexstyl.specialdates.addevent

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.OperationApplicationException
import android.os.RemoteException
import android.provider.ContactsContract

import com.alexstyl.specialdates.CrashAndErrorTracker

import java.util.ArrayList

class AndroidContactOperationsExecutor(
        private val contentResolver: ContentResolver,
        private val tracker: CrashAndErrorTracker) : ContactOperationsExecutor {

    override fun execute(operations: ArrayList<ContentProviderOperation>): Boolean {
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            return true
        } catch (e: RemoteException) {
            tracker.track(e)
        } catch (e: OperationApplicationException) {
            tracker.track(e)
        }
        return false
    }

}
