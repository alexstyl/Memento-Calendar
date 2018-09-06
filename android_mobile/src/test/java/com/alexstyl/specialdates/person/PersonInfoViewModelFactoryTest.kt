package com.alexstyl.specialdates.person

import com.alexstyl.resources.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.JULY
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PersonInfoViewModelFactoryTest {

    private val strings = JavaStrings()
    private val toViewModel = PersonDetailsViewModelFactory(strings, AgeCalculator(dateOn(30, JULY, 2017)));

    @Test
    fun whenPassingAContact_thenAlwaysReturnItsName() {
        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), null, true)
        assertThat(resultViewModel.displayName).isEqualTo("Anna Roberts")
    }

    @Test
    fun whenPassingNoContactEvent_thenAgeAndStarSignIsEmptyString() {
        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), null, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("")
    }

    @Test
    fun whenPassingABirthdayWithoutYear_thenAgeAndStarSignContainsOnlyStarSign() {
        val dateOfBirth = dateOn(1, JANUARY)
        val contactEvent = ContactEvent(StandardEventType.BIRTHDAY, dateOfBirth, aContactCalled("Anna Roberts"), Optional.absent())

        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), contactEvent, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("Capricorn ♑")
    }

    @Test
    fun whenPassingABirthdayWithYear_thenAgeAndStarSignContainsBothAgeAndStarSign() {
        val dateOfBirth = dateOn(1, JANUARY, 1990)
        val contactEvent = ContactEvent(StandardEventType.BIRTHDAY, dateOfBirth, aContactCalled("Anna Roberts"), Optional.absent())

        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), contactEvent, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("27, Capricorn ♑")
    }


    fun aContactCalled(peter: String): Contact {
        return Contact(-1, DisplayName.from(peter), "https://www.alexstyl.com/image.jpg", 1)
    }

}
