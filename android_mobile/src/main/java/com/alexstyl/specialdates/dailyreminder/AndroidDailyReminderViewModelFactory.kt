package com.alexstyl.specialdates.dailyreminder

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.util.NaturalLanguageUtils

class AndroidDailyReminderViewModelFactory(private val strings: Strings,
                                           private val todaysDate: Date,
                                           private val colors: Colors)
    : DailyReminderViewModelFactory {

    override fun summaryOf(viewModels: List<ContactEventNotificationViewModel>): SummaryNotificationViewModel {
        val contacts = viewModels.fold(emptyList<Contact>(), { list, viewModel ->
            list + viewModel.contactEvent.contact
        })

        val title = NaturalLanguageUtils.joinContacts(strings, contacts, MAX_CONTACTS)
        val label = TextUtils.join(", ", contacts)

        return SummaryNotificationViewModel(
                NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY,
                title, label
        )
    }

    override fun viewModelFor(contactEvent: ContactEvent): ContactEventNotificationViewModel {
        val contact = contactEvent.contact
        val coloredLabel = SpannableString(contactEvent.getLabel(todaysDate, strings)).apply {
            setSpan(ForegroundColorSpan(colors.getColorFor(contactEvent.type)), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return ContactEventNotificationViewModel(contact.hashCode(),
                contactEvent,
                contact.displayName.toString(),
                coloredLabel,
                arrayListOf(     // TODO check if they actually have numbers/emails to call/send wishes
                        ContactActionViewModel(
                                (ActionType.CALL.hashCode() + contact.contactID).toInt(), strings.call(), ActionType.CALL),
                        ContactActionViewModel(
                                (ActionType.SEND_WISH.hashCode() + contact.contactID).toInt(), strings.sendWishes(), ActionType.SEND_WISH))
        )
    }

    override fun viewModelFor(namedays: NamesInADate): NamedaysNotificationViewModel {
        return NamedaysNotificationViewModel(namedays.date,
                strings.todaysNamedays(namedays.getNames().size),
                namedays.getNames().joinToString(", "))
    }

    override fun viewModelFor(bankHoliday: BankHoliday): BankHolidayNotificationViewModel =
            BankHolidayNotificationViewModel(bankHoliday.holidayName, strings.bankholidaySubtitle())


    companion object {
        private const val MAX_CONTACTS = 3
    }

}
