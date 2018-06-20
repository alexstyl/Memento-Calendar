package com.alexstyl.specialdates.addevent.ui

import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.contact.Contacts
import org.mockito.BDDMockito

fun BDDMockito.BDDMyOngoingStubbing<Contacts>.willReturnContacts(name: String, vararg names: String) =
        this.willReturn((Contacts(1, (names.toList() + name).map { ContactFixture.aContactCalled(it) })))

fun BDDMockito.BDDMyOngoingStubbing<Contacts>.willReturnNoContact() =
        this.willReturn((Contacts(1, emptyList())))
