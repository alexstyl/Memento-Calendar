package com.alexstyl.specialdates.permissions

import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_CONTACTS
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.alexstyl.specialdates.CrashAndErrorTracker

class AndroidPermissions(private val tracker: CrashAndErrorTracker, private val context: Context) : MementoPermissions {

    override fun canReadAndWriteContacts(): Boolean {
        return canReadContacts() && hasPermission(context, WRITE_CONTACTS, tracker)
    }

    override fun canReadContacts(): Boolean {
        return hasPermission(context, READ_CONTACTS, tracker)
    }

    override fun canReadExternalStorage(): Boolean {
        return hasPermission(context, READ_EXTERNAL_STORAGE, tracker)
    }

    private fun hasPermission(context: Context, permission: String, tracker: CrashAndErrorTracker): Boolean {
        return try {
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (ex: RuntimeException) {
            // some devices randomly throw an exception to this point. just treat as if the permission is not there
            tracker.track(ex)
            false
        }
    }
}
