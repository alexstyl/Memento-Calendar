package com.alexstyl.specialdates.permissions


interface MementoPermissions {
    fun canReadAndWriteContacts(): Boolean
    fun canReadExternalStorage(): Boolean
}
