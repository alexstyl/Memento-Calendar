package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.util.HashMapList

class UpcomingRowViewModelsBuilder(private val duration: TimePeriod,
                                   private val viewModelFactory: UpcomingEventRowViewModelFactory,
                                   private val adRules: UpcomingEventsAdRules
) {

    private val dateComparator = DateComparator.INSTANCE
    private val contactEvents = HashMapList<Date, ContactEvent>()
    private val namedays = HashMap<Date, NamesInADate>()
    private val bankHolidays = HashMap<Date, BankHoliday>()

    fun withContactEvents(contactEvents: List<ContactEvent>): UpcomingRowViewModelsBuilder {
        this.contactEvents.clear()
        for (contactEvent in contactEvents) {
            val annualDate = contactEvent.date
            this.contactEvents.addValue(annualDate, contactEvent)
        }
        return this
    }

    fun withNamedays(namedays: List<NamesInADate>): UpcomingRowViewModelsBuilder {
        this.namedays.clear()
        for (nameday in namedays) {
            val date = nameday.date
            this.namedays.put(date, nameday)
        }
        return this
    }

    fun withBankHolidays(bankHolidays: List<BankHoliday>): UpcomingRowViewModelsBuilder {
        this.bankHolidays.clear()
        for (bankHoliday in bankHolidays) {
            val date = bankHoliday.date
            this.bankHolidays.put(date, bankHoliday)
        }
        return this
    }

    fun build(): List<UpcomingRowViewModel> {
        if (noEventsArePresent()) {
            return NO_CELEBRATIONS
        }

        val rowsViewModels = ArrayList<UpcomingRowViewModel>()
        var indexDate = duration.startingDate
        val lastDate = duration.endingDate

        var index = 0
        while (dateComparator.compare(indexDate, lastDate) <= 0) {
            val annualDate = indexDate
            if (containsAnyEventsOn(annualDate)) {

                rowsViewModels.add(viewModelFactory.createDateHeader(indexDate))

                if (bankHolidays[annualDate] != null) {
                    rowsViewModels.add(viewModelFactory.createViewModelFor(bankHolidays[annualDate]!!))
                }
                if (namedays[annualDate] != null) {
                    rowsViewModels.add(viewModelFactory.createViewModelFor(namedays[annualDate]!!))
                }

                getEventsOn(annualDate).forEach {
                    rowsViewModels.add(viewModelFactory.createViewModelFor(it))

                }
                if (adRules.shouldAppendAd()) {
                    rowsViewModels.add(AdViewModel(indexDate))
                    adRules.onNewAdAdded()
                }
                index++
            }
            indexDate = indexDate.addDay(1)
        }
        return rowsViewModels
    }

    private fun getEventsOn(indexDate: Date): List<ContactEvent> {
        val contactEvent = contactEvents.get(indexDate)
        if (contactEvent == null) {
            return NO_CONTACT_EVENTS
        } else {
            return contactEvent
        }
    }

    private fun noEventsArePresent(): Boolean {
        return contactEvents.isEmpty && namedays.isEmpty() && bankHolidays.isEmpty()
    }

    private fun containsAnyEventsOn(date: Date): Boolean {
        return getEventsOn(date).size > 0 || namedays.containsKey(date) || bankHolidays.containsKey(date)
    }

    companion object {

        private val NO_CELEBRATIONS = emptyList<UpcomingRowViewModel>()
        private val NO_CONTACT_EVENTS = emptyList<ContactEvent>()
    }
}
