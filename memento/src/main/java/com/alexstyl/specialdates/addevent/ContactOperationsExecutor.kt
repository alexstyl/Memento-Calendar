package com.alexstyl.specialdates.addevent

import java.util.ArrayList

interface ContactOperationsExecutor {
    fun execute(operations: ArrayList<out ContactOperations>): Boolean
}
