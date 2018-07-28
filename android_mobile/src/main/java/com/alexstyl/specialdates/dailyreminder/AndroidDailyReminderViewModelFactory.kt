package com.alexstyl.specialdates.dailyreminder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NoNamesInADate
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.util.NaturalLanguageUtils
import java.net.URI

class AndroidDailyReminderViewModelFactory(private val strings: Strings,
                                           private val todaysDate: Date,
                                           private val colors: Colors)
    : DailyReminderViewModelFactory {


    override fun summaryOf(viewModels: List<ContactEventNotificationViewModel>): SummaryNotificationViewModel {
        val contacts = viewModels.fold(emptyList<Contact>()) { list, viewModel ->
            list + viewModel.contact
        }

        val title = NaturalLanguageUtils.joinContacts(strings, contacts, MAX_CONTACTS)
        val label = strings.dontForgetToSendWishes()

        val lines = arrayListOf<CharSequence>()
        viewModels.forEach { contactViewModel ->

            val boldedTitle = SpannableString("${contactViewModel.title}\t\t${contactViewModel.label}").apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, contactViewModel.title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            lines.add(boldedTitle)
        }

        val images = viewModels.fold(emptyList<URI>(), { list, viewModel ->
            list + viewModel.contact.imagePath
        })

        return SummaryNotificationViewModel(
                NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY,
                title, label, lines, images
        )
    }

    override fun viewModelFor(contact: Contact, events: List<ContactEvent>): ContactEventNotificationViewModel {
        val stringBuilder = SpannableStringBuilder()
        events.forEach { contactEvent ->
            val coloredLabel = SpannableString(contactEvent.getLabel(todaysDate, strings) + " " + emojiFor(contactEvent)).apply {
                setSpan(ForegroundColorSpan(colors.getColorFor(contactEvent.type)), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(", ")
            }
            stringBuilder.append(coloredLabel)
        }

        return ContactEventNotificationViewModel(events.hashCode(),
                contact,
                contact.displayName.toString(),
                stringBuilder,
                emptyList() // TODO feature coming from the notification_actions branch
        )
    }

    private fun emojiFor(contactEvent: ContactEvent): CharSequence =
            when (contactEvent.type) {
                StandardEventType.BIRTHDAY -> "🍰"
                StandardEventType.NAMEDAY -> "🎈"
                StandardEventType.ANNIVERSARY -> "💍"
                StandardEventType.OTHER -> "🌸"
                else -> ""
            }

    override fun viewModelFor(namedays: NoNamesInADate): NamedaysNotificationViewModel {
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
