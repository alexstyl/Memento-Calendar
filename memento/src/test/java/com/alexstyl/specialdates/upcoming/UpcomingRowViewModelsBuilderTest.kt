package com.alexstyl.specialdates.upcoming

import com.alexstyl.TestColors
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.ContactFixture.aContact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.Months.FEBRUARY
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.upcoming.widget.list.NoAds
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.util.*
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
        `when`(mockColors!!.getTodayHeaderTextColor()).thenReturn(5)
        val today = Date.today()
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
                .withContactEvents(asList(aContactEventOn(Date.on(1, JANUARY, 2016))))
                .build()

        assertThat(viewModels.size).isEqualTo(2)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(UpcomingContactEventViewModel::class.java)
    }

    @Test
    fun givenTwoContactEvents_whenBothAreOnTheSameDate_thenCreatesADateHeaderPlusTwoContactEvents() {
        val viewModels = builderFor(entireYear(1990))
                .withContactEvents(asList(
                        aContactEventOn(Date.on(1, FEBRUARY, 1990)),
                        aContactEventOn(Date.on(1, FEBRUARY, 1990))
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
                        aContactEventOn(Date.on(1, FEBRUARY, 1990)),
                        aContactEventOn(Date.on(3, FEBRUARY, 1990))
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
        val date = Date.on(1, Months.APRIL, 2017)
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
        val date = Date.on(1, Months.APRIL, 2017)

        val bankHoliday = BankHoliday("A bank holiday", date)
        val bankHolidays = listOf(bankHoliday)
        val namedays = namedayOf(date, "Maria")

        val viewModels = builderWithAds(entireYear(2017))
                .withContactEvents(asList(
                        aContactEventOn(Date.on(1, Months.APRIL, 2017)),
                        aContactEventOn(Date.on(2, Months.APRIL, 2017))
                ))
                .withNamedays(namedays)
                .withBankHolidays(bankHolidays)
                .build()

        assertThat(viewModels.size).isEqualTo(7)
        assertThat(viewModels[0]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[1]).isInstanceOf(BankHolidayViewModel::class.java)
        assertThat(viewModels[2]).isInstanceOf(UpcomingNamedaysViewModel::class.java)
        assertThat(viewModels[3]).isInstanceOf(UpcomingContactEventViewModel::class.java)
        assertThat(viewModels[4]).isInstanceOf(AdViewModel::class.java)
        assertThat(viewModels[5]).isInstanceOf(DateHeaderViewModel::class.java)
        assertThat(viewModels[6]).isInstanceOf(UpcomingContactEventViewModel::class.java)
    }

    private fun namedayOf(date: Date, maria: String): List<NamesInADate> {
        val namedays = ArrayList<NamesInADate>()
        namedays.add(NamesInADate(date, listOf(maria)))
        return namedays
    }

    private fun builderFor(timePeriod: TimePeriod): UpcomingRowViewModelsBuilder {
        return UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory!!, NoAds())
    }

    private fun builderWithAds(timePeriod: TimePeriod): UpcomingRowViewModelsBuilder {
        return UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory!!, UpcomingEventsFreeUserAdRules())
    }


    private fun aBankHoliday(): BankHoliday {
        return BankHoliday("A bank holiday", Date.on(1, JANUARY, 1990))
    }

    private fun aContactEventOn(date: Date): ContactEvent {
        return ContactEvent(NO_DEVICE_EVENT_ID, StandardEventType.BIRTHDAY, date, aContact())
    }

    private fun entireYear(year: Int): TimePeriod {
        return TimePeriod.between(Date.startOfYear(year), Date.endOfYear(year))
    }

    companion object {
        private val NO_DEVICE_EVENT_ID = Optional.absent<Long>()
    }

}

class DateStringCreator : UpcomingDateStringCreator {
    override fun createLabelFor(date: Date): String = "Header Label"

}
