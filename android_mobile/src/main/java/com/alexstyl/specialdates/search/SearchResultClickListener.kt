package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date

interface SearchResultClickListener {
    fun onContactClicked(contact: Contact)
    fun onNamedayClicked(date: Date)
    fun onContactReadPermissionClicked()
}