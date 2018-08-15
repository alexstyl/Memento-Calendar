package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.JULY
import com.alexstyl.specialdates.date.Months.JUNE
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.api.Assertions.fail
import org.json.JSONException
import org.junit.Before
import org.junit.Test

class NamedayJSONParserTest {

    private var namedayJSON: NamedayJSON? = null

    @Before
    @Throws(JSONException::class)
    fun setUp() {
        val resourceLoader = TestJSONResourceLoader()
        val resourceProvider = NamedayJSONProvider(resourceLoader)
        namedayJSON = resourceProvider.getNamedayJSONFor(NamedayLocale.GREEK)

    }

    @Test
    fun returningBundleHasNames() {
        val namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON)
        assertThat(namedayBundle.names).isNotEmpty
    }

    @Test
    fun alexandrosNamedayIsReturnedCorrectly() {
        val namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON)
        val dates = namedayBundle.getDatesFor("Αλέξανδρος")
        assertThatContainsDate(dates, dateOn(30, AUGUST))
    }

    @Test
    fun davidNamedayIsReturnedCorrectly() {
        val namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON)
        val dates = namedayBundle.getDatesFor("Δαβίδ")
        assertThatContainsDate(dates, dateOn(26, JUNE))
    }

    @Test
    fun amaliaNamedayIsReturnedCorrectly() {
        val namedayBundle = NamedayJSONParser.getNamedaysFrom(namedayJSON)
        val dates = namedayBundle.getDatesFor("Αμαλία")
        assertThatContainsDate(dates, dateOn(10, JULY))
    }

    private fun assertThatContainsDate(celebrations: NameCelebrations, date: Date) {
        val dates = celebrations.dates
        var index = 0
        while (index < dates.size) {
            if (celebrations.dates[index] == date) {
                return
            }
            index++
        }

        fail("Couldn't find date $date inside $celebrations")
    }
}
