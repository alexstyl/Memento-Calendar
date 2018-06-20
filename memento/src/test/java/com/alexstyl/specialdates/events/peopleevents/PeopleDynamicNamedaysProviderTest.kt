package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.contact.Contacts
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.namedays.calendar.resource.TestNamedayCalendarBuilder
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PeopleDynamicNamedaysProviderTest {

    private lateinit var calculator: PeopleDynamicNamedaysProvider
    @Mock
    private lateinit var namedayCalendarProvider: NamedayCalendarProvider
    @Mock
    private lateinit var mockSettings: NamedayUserSettings
    @Mock
    private lateinit var mockSource: ContactsProviderSource
    private val EASTER_CELEBRATING_CONTACT = ContactFixture.aContactCalled("Λάμπρος")

    @Before
    fun setUp() {
        val namedayCalendar = TestNamedayCalendarBuilder()
                .forLocale(LOCALE)
                .forYear(YEAR)
                .build()

        given(namedayCalendarProvider.loadNamedayCalendarForLocale(any(NamedayLocale::class.java), any(Int::class.java))).willReturn(namedayCalendar)
        given(mockSettings.selectedLanguage).willReturn(LOCALE)
        given(mockSettings.isEnabled).willReturn(true)
        calculator = PeopleDynamicNamedaysProvider(mockSettings, namedayCalendarProvider,
                ContactsProvider(mapOf(Pair(1, mockSource)))
        )

    }

    @Test
    fun gettingSpecialNamedaysOnSpecificDateOnlyReturnsTheEventsForThatDate() {
        val testContacts = createSomeContacts()
        testContacts.add(EASTER_CELEBRATING_CONTACT)
        given(mockSource.allContacts).willReturn(Contacts(1, testContacts))

        val orthodoxEasterCalculator = OrthodoxEasterCalculator()
        val easterDate = orthodoxEasterCalculator.calculateEasterForYear(YEAR)
        val contactEvents = calculator.fetchEventsBetween(TimePeriod.between(easterDate, easterDate))
        assertThat(contactEvents).hasSize(1)
        assertThat(contactEvents[0].contact).isEqualTo(EASTER_CELEBRATING_CONTACT)
    }

    private fun createSomeContacts(): MutableList<Contact> = arrayListOf(
            ContactFixture.aContactCalled("Αβδηρος"),
            ContactFixture.aContactCalled("Αγις")
    )

    companion object {

        private val LOCALE = NamedayLocale.GREEK

        private const val YEAR = 2016
    }

}
