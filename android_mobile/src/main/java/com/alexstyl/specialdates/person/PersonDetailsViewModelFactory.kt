package com.alexstyl.specialdates.person

import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

internal class PersonDetailsViewModelFactory(val stringResources: StringResources) {

    operator fun invoke(contact: Contact, contactEvent: ContactEvent?): PersonDetailsViewModel {
        val ageAndStarSignBuilder = StringBuilder()
        if (contactEvent != null) {
            ageAndStarSignBuilder.append(ageOf(contactEvent.date!!))
            if (ageAndStarSignBuilder.isNotEmpty()) {
                ageAndStarSignBuilder.append(", ")
            }
            ageAndStarSignBuilder.append(starSignOf(contactEvent.date!!))
        }
        return PersonDetailsViewModel(contact.displayName.toString(), ageAndStarSignBuilder.toString(), contact.imagePath)
    }

    private fun ageOf(birthday: Date): String {
        if (birthday.hasYear()) {
            return 24.toString()
        }
        return ""
    }

    private fun starSignOf(birthday: Date): String {
        val starSign = StarSign.forDateOfBirth(birthday)
        return stringResources.getString(starSign.labelStringRes);
    }
}

