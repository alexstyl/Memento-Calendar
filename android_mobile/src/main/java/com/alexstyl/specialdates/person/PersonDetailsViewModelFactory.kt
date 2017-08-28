package com.alexstyl.specialdates.person

import com.alexstyl.resources.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

internal class PersonDetailsViewModelFactory(private val strings: Strings, private val ageCalculator: AgeCalculator) {

    operator fun invoke(contact: Contact, contactEvent: ContactEvent?): PersonInfoViewModel {
        val ageAndStarSignBuilder = StringBuilder()
        if (contactEvent != null) {
            ageAndStarSignBuilder.append(ageCalculator.ageOf(contactEvent.date!!))
            if (ageAndStarSignBuilder.isNotEmpty()) {
                ageAndStarSignBuilder.append(", ")
            }
            ageAndStarSignBuilder.append(starSignOf(contactEvent.date!!))
        }
        return PersonInfoViewModel(contact.displayName.toString(), ageAndStarSignBuilder.toString(), contact.imagePath)
    }

    private fun starSignOf(birthday: Date): String {
        val starSign = StarSign.forDateOfBirth(birthday)
        return strings.getString(starSign.labelStringRes) + " " + starSign.emoji
    }
}


