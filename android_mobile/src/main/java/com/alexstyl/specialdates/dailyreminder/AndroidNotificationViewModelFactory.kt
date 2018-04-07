package com.alexstyl.specialdates.dailyreminder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate

class AndroidNotificationViewModelFactory(private val strings: Strings,
                                          private val todaysDate: Date,
                                          val colors: Colors,
                                          val userSettings: DailyReminderUserSettings)
    : NotificationViewModelFactory {


    override fun contactEventsViewModel(contactEvent: ContactEvent): ContactEventNotificationViewModel {
        val contact = contactEvent.contact
        val coloredLabel = SpannableString(contactEvent.getLabel(todaysDate, strings)).apply {
            setSpan(ForegroundColorSpan(colors.getColorFor(contactEvent.type)), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return ContactEventNotificationViewModel(contact.hashCode(),
                contact,
                contact.displayName.toString(),
                coloredLabel,
                userSettings.getRingtone()
        )
    }

    fun namedaysViewModel(namedays: NamesInADate): NamedaysNotificationViewModel =
            NamedaysNotificationViewModel(namedays.date, namedays.getNames())


    fun forBankHoliday(bankHoliday: BankHoliday?): BankHolidayNotificationViewModel? =
            if (bankHoliday != null)
                BankHolidayNotificationViewModel(bankHoliday.date, bankHoliday.holidayName)
            else null


    fun summaryOf(t1: List<ContactEventNotificationViewModel>, t2: NamedaysNotificationViewModel, t3: BankHolidayNotificationViewModel) =
            SummaryNotificationViewModel("something")


}
