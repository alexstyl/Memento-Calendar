package com.alexstyl.specialdates.person

class ContactAction(val value: String, val label: String, val action: () -> Unit) {
    fun run() {
        action()
    }
}
