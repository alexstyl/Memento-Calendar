package com.alexstyl.specialdates.person

import android.view.View
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

internal class PersonDetailsViewModelFactory(private val strings: Strings, private val ageCalculator: AgeCalculator) {

    operator fun invoke(contact: Contact, dateOfBirth: ContactEvent?, isVisible: Boolean): PersonInfoViewModel {
        val ageAndStarSignBuilder = StringBuilder()
        if (dateOfBirth != null) {
            ageAndStarSignBuilder.append(ageCalculator.ageOf(dateOfBirth.date))
            if (ageAndStarSignBuilder.isNotEmpty()) {
                ageAndStarSignBuilder.append(", ")
            }
            ageAndStarSignBuilder.append(starSignOf(dateOfBirth.date))
        }
        return PersonInfoViewModel(contact.displayName.toString(),
                ageAndStarSignBuilder.toString(),
                if (ageAndStarSignBuilder.isEmpty()) View.GONE else View.VISIBLE,
                contact.imagePath,
                isVisible)
    }

    private fun starSignOf(birthday: Date): String {
        val starSign = StarSign.forDateOfBirth(birthday)
        return strings.nameOf(starSign) + " " + starSign.emoji
    }
}


