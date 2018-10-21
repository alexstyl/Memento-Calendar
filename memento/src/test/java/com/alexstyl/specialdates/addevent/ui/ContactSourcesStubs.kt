package com.alexstyl.specialdates.addevent.ui

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactFixture
import org.mockito.BDDMockito

fun BDDMockito.BDDMyOngoingStubbing<List<Contact>>.willReturnContacts(name: String, vararg names: String) =
        this.willReturn((names.toList() + name).map { ContactFixture.aContactCalled(it) })

fun BDDMockito.BDDMyOngoingStubbing<List<Contact>>.willReturnNoContact() = this.willReturn(emptyList())
