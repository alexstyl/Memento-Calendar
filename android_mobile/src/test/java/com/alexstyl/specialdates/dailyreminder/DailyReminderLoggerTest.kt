package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.log.DailyReminderLogger
import com.alexstyl.specialdates.dailyreminder.log.LoggedEventsRepository
import com.alexstyl.specialdates.date.Date
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.net.URI

class DailyReminderLoggerTest {

    private lateinit var logger: DailyReminderLogger

    private val mockRepository = mock(LoggedEventsRepository::class.java)

    @Before
    fun setup() {
        logger = DailyReminderLogger(TestDateLabelCreator.forUS(), mockRepository)
    }

    @Test
    fun noEvents() {
        val lineOf = logger.lineOf(dateOn(20, 2, 1990), emptyModel())
        assertThat(lineOf).isEqualTo("February 20 1990  –  Bank Holiday: None\nNamedays: None\nContacts: None")
    }

    @Test
    fun justContacts() {
        val viewModel = summaryModelOf("Alex Styl".asContact())
        val lineOf = logger.lineOf(dateOn(20, 2, 1990), viewModel)
        assertThat(lineOf).isEqualTo("February 20 1990  –  Bank Holiday: None\nNamedays: None\nContacts: Alex Styl")
    }

    private fun summaryModelOf(contact: Contact): DailyReminderViewModel {
        return DailyReminderViewModel(SummaryNotificationViewModel(0, "", "", listOf(), listOf()),
                listOf(ContactEventNotificationViewModel(0, contact, "Title", "Label", listOf())),
                Optional.absent(),
                Optional.absent())
    }

    private fun emptyModel(): DailyReminderViewModel {
        return DailyReminderViewModel(SummaryNotificationViewModel(0, "", "", listOf(), listOf()),
                listOf(),
                Optional.absent(),
                Optional.absent())
    }


}

private fun String.asContact(): Contact {
    return Contact(2, DisplayName.from("Alex Styl"), URI.create(""), ContactSource.SOURCE_DEVICE)
}
