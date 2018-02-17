package com.alexstyl.specialdates.permissions


interface MementoPermissionsChecker {
    fun canReadAndWriteContacts(): Boolean
    fun canReadExternalStorage(): Boolean
}
