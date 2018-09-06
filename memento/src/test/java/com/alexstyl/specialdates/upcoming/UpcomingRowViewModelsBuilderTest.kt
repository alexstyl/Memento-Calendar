package com.alexstyl.specialdates.upcoming

import com.alexstyl.TestColors
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.contact.ContactFixture.aContact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.Months.FEBRUARY
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.date.beggingOfYear
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.endOfYear
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.ArrayNamesInADate
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import java.util.Arrays.asList

@RunWith(MockitoJUnitRunner::class)
class UpcomingRowViewModelsBuilderTest {

    private var upcomingEventRowViewModelFactory: UpcomingEventRowViewModelFactory? = null
    private val mockStrings = JavaStrings()

    @Mock
    private val mockColors: Colors? = TestColors()
    private val mockDateStringCreator = DateStringCreator()

    @Before
    fun setUp() {
        given(mockColors!!.getTodayHeaderTextColor()).willReturn(5)
        val today = todaysDate()
        upcomingEventRowViewModelFactory = UpcomingEventRowViewModelFactory(
                today,
                mockColors,
                mockDateStringCreator,
                ContactViewModelFactory(mockColors, mockStrings)
        )

    }

    @Test
    fun givenASingleContactEvent_thenCreatesADateHeaderPlusAContactEvent() {

        val viewModels = builderFor(entireYear(2016))
                .withContactEvents(asList(aContactEventOn(dateOn(1, JANUARY, 2016))))
                .build()

        assertThat(viewModels.size).isEqualTo(2)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(UpcomingContactEventViewModel::class.java)
    }

    @Test
    fun givenTwoContactEvents_whenBothAreOnTheSameDate_thenCreatesADateHeaderPlusTwoContactEvents() {
        val viewModels = builderFor(entireYear(1990))
                .withContactEvents(asList(
                        aContactEventOn(dateOn(1, FEBRUARY, 1990)),
                        aContactEventOn(dateOn(1, FEBRUARY, 1990))
                ))
                .build()

        assertThat(viewModels.size).isEqualTo(3)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(UpcomingContactEventViewModel::class.java)
        assertThat(viewModels[2]).isInstanceOf(UpcomingContactEventViewModel::class.java)
    }

    @Test
    fun givenTwoContactEvents_whenBothAreOnDifferentDate_thenCreatesADateHeaderAndAContactEventForEachOne() {
        val viewModels = builderFor(entireYear(1990))
                .withContactEvents(asList(
                        aContactEventOn(dateOn(1, FEBRUARY, 1990)),
                        aContactEventOn(dateOn(3, FEBRUARY, 1990))
                ))
                .build()

        assertThat(viewModels.size).isEqualTo(4)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(UpcomingContactEventViewModel::class.java)
        assertThat(viewModels[2]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[3]).isInstanceOf(UpcomingContactEventViewModel::class.java)
    }

    @Test
    fun givenABankHoliday_thenCreatesADateHeaderPlusABankholidayModel() {
        val viewModels = builderFor(TimePeriod.between(aBankHoliday().date, aBankHoliday().date))
                .withBankHolidays(asList(aBankHoliday()))
                .build()

        assertThat(viewModels.size).isEqualTo(2)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(BankHolidayViewModel::class.java)
    }

    @Test
    fun givenANameday_thenCreatesADateHeaderPlusANamedayModel() {
        val date = dateOn(1, Months.APRIL, 2017)
        val namedays = namedayOf(date, "Maria")

        val viewModels = builderFor(TimePeriod.between(date, date))
                .withNamedays(namedays)
                .build()

        assertThat(viewModels.size).isEqualTo(2)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(UpcomingNamedaysViewModel::class.java)
    }

    @Test
    fun theDisplayOrderIsCorrect() {
        val date = dateOn(1, Months.APRIL, 2017)

        val bankHoliday = BankHoliday("A bank holiday", date)
        val bankHolidays = listOf(bankHoliday)
        val namedays = namedayOf(date, "Maria")

        val viewModels = builderWithAds(entireYear(2017))
                .withContactEvents(asList(
                        aContactEventOn(dateOn(1, Months.APRIL, 2017)),
                        aContactEventOn(dateOn(2, Months.APRIL, 2017))
                ))
                .withNamedays(namedays)
                .withBankHolidays(bankHolidays)
                .build()

        assertListContainsOrderedItems(viewModels, asList(
                DateHeaderViewModel::class.java,
                BankHolidayViewModel::class.java,
                UpcomingNamedaysViewModel::class.java,
                UpcomingContactEventViewModel::class.java,
                DateHeaderViewModel::class.java,
                UpcomingContactEventViewModel::class.java
        ))
    }

    private fun assertListContainsOrderedItems(viewModels: List<UpcomingRowViewModel>,
                                               classes: List<Class<out UpcomingRowViewModel>>) {
        classes.forEachIndexed { index, clazz ->
            assertThat(viewModels[index]).isInstanceOf(clazz)
        }
        assertThat(viewModels.size).isEqualTo(classes.size)
    }

    private fun namedayOf(date: Date, maria: String): List<NamesInADate> {
        return listOf(ArrayNamesInADate(date, arrayListOf(maria)))
    }

    private fun builderFor(timePeriod: TimePeriod): UpcomingRowViewModelsBuilder {
        return UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory!!)
    }

    private fun builderWithAds(timePeriod: TimePeriod): UpcomingRowViewModelsBuilder {
        return UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory!!)
    }


    private fun aBankHoliday(): BankHoliday {
        return BankHoliday("A bank holiday", dateOn(1, JANUARY, 1990))
    }

    private fun aContactEventOn(date: Date): ContactEvent {
        return ContactEvent(StandardEventType.BIRTHDAY, date, aContact(), NO_DEVICE_EVENT_ID)
    }

    private fun entireYear(year: Int): TimePeriod {
        return TimePeriod.between(beggingOfYear(year), endOfYear(year))
    }

    companion object {
        private val NO_DEVICE_EVENT_ID: Long? = null
    }

}


class DateStringCreator : UpcomingDateStringCreator {
    override fun createLabelFor(date: Date): String = "Header Label"

}
