package com.alexstyl.specialdates.person

import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateConstants.JANUARY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PersonInfoViewModelFactoryTest {

    private val toViewModel = PersonDetailsViewModelFactory(mock(StringResources::class.java));

    @Test
    fun whenPassingAContact_thenAlwaysReturnItsName() {
        var resultViewModel = toViewModel(ContactFixture.withName("Anna Roberts"), null)
        assertThat(resultViewModel.displayName).isEqualTo("Anna Roberts")
    }

    @Test
    fun whenPassingNoContactEvent_thenAgeAndStarSignIsEmptyString() {
        var resultViewModel = toViewModel(ContactFixture.withName("Anna Roberts"), null)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("")
    }

    @Test
    fun whenPassingABirthdayWithOutYear_thenAgeAndStarSignContainsOnlyStarSign() {
        val dateOfBirth = Date.on(1, JANUARY)
        val contactEvent = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, dateOfBirth, ContactFixture.withName("Anna Roberts"))

        var resultViewModel = toViewModel(ContactFixture.withName("Anna Roberts"), contactEvent)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("Sagittarius")
    }

    @Test
    fun whenPassingABirthdayWithYear_thenAgeAndStarSignContainsBothAgeAndStarSign() {
        val dateOfBirth = Date.on(1, JANUARY, 1990)
        val contactEvent = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, dateOfBirth, ContactFixture.withName("Anna Roberts"))

        var resultViewModel = toViewModel(ContactFixture.withName("Anna Roberts"), contactEvent)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("26, Sagittarius")
    }

}
