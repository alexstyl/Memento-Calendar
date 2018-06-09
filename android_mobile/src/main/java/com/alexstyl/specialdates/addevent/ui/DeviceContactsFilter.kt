package com.alexstyl.specialdates.addevent.ui

import android.widget.Filter

import com.alexstyl.specialdates.addevent.ContactsSearch
import com.alexstyl.specialdates.contact.Contact

import java.util.ArrayList

internal abstract class DeviceContactsFilter(private val contactsSearch: ContactsSearch) : Filter() {

    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        if (constraint == null || constraint.isEmpty()) {
            return emptyResults()
        }
        val searchQuery = constraint.trim { it <= ' ' }.toString()
        val contacts = contactsSearch.searchForContacts(searchQuery, LOAD_A_SINGLE_CONTACT)

        val filterResults = Filter.FilterResults()
        filterResults.values = contacts
        filterResults.count = contacts.size
        return filterResults
    }

    private fun emptyResults(): Filter.FilterResults {
        val filterResults = Filter.FilterResults()
        filterResults.values = ArrayList<Any>()
        filterResults.count = 0
        return filterResults
    }

    override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
        onContactsFiltered(results.values as List<Contact>)
    }

    abstract fun onContactsFiltered(contacts: List<Contact>)

    companion object {

        private val LOAD_A_SINGLE_CONTACT = 1
    }
}
