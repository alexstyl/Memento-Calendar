package com.alexstyl.specialdates.permissions


interface MementoPermissions {
    fun canReadAndWriteContacts(): Boolean
    fun canReadContacts(): Boolean
    fun canReadExternalStorage(): Boolean
}
