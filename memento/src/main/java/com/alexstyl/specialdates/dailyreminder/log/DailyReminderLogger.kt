package com.alexstyl.specialdates.dailyreminder.log

import com.alexstyl.specialdates.Optional


import com.alexstyl.specialdates.dailyreminder.BankHolidayNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.ContactEventNotificationViewModel
import com.alexstyl.specialdates.dailyreminder.DailyReminderViewModel
import com.alexstyl.specialdates.dailyreminder.NamedaysNotificationViewModel
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator

class DailyReminderLogger(val dateLabelCreator: DateLabelCreator,
                          private val repository: LoggedEventsRepository) {

    fun clearAll() {
        repository.writeEvents("")
    }

    fun appendEvents(date: Date, viewModel: DailyReminderViewModel) {
        val whatToWrite = fetchAllEvents() + lineOf(date, viewModel) + DAYS_SEPARATOR
        repository.writeEvents(whatToWrite)
    }

    fun fetchAllEvents(): String {
        return repository.fetchAllEvents()
    }

    fun lineOf(date: Date, dailyReminderViewModel: DailyReminderViewModel): String {
        val dateLabel = dateLabelCreator.createWithYearPreferred(date)
        val viewModelsLabel =
                "Bank Holiday: ${dailyReminderViewModel.bankHoliday.asString()}" +
                        "\nNamedays: ${stringOf(dailyReminderViewModel.namedays)}" +
                        "\nContacts: ${stringOf(dailyReminderViewModel.contacts)}"
        return "$dateLabel  –  $viewModelsLabel"
    }

    private fun stringOf(list: List<ContactEventNotificationViewModel>) = if (list.isNotEmpty()) {
        list.joinToString { it -> it.contact.displayName.toString() }
    } else {
        "None"
    }


    private fun stringOf(optional: Optional<NamedaysNotificationViewModel>) =
            if (optional.isPresent) {
                optional.get().label
            } else {
                "None"
            }

    private fun Optional<BankHolidayNotificationViewModel>.asString() =
            if (isPresent) {
                get().label
            } else {
                "None"
            }

    companion object {
        private const val DAYS_SEPARATOR = "\n\n\n"
    }
}

