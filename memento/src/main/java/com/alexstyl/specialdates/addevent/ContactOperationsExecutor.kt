package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.addevent.operations.ContactOperation

interface ContactOperationsExecutor {
    fun execute(operations: List<ContactOperation>): Boolean
}
