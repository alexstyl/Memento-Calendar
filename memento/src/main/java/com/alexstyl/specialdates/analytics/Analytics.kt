package com.alexstyl.specialdates.analytics

import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.donate.Donation
import com.alexstyl.specialdates.events.peopleevents.EventType

interface Analytics {
    fun trackThemeSelected(string: String)

    fun trackScreen(screen: Screen)

    fun trackAddEventsCancelled()

    fun trackEventAddedSuccessfully()

    fun trackContactSelected()

    fun trackEventDatePicked(eventType: EventType)

    fun trackEventRemoved(eventType: EventType)

    fun trackImageCaptured()

    fun trackExistingImagePicked()

    fun trackAvatarSelected()

    fun trackContactUpdated()

    fun trackContactCreated()

    fun trackDailyReminderEnabled()

    fun trackDailyReminderDisabled()

    fun trackDailyReminderTimeUpdated(timeOfDay: TimeOfDay)

    fun trackWidgetAdded(widget: Widget)

    fun trackWidgetRemoved(widget: Widget)

    fun trackDonationStarted(donation: Donation)

    fun trackAppInviteRequested()

    fun trackDonationRestored()

    fun trackDonationPlaced(donation: Donation)

    fun trackFacebookLoggedIn()

    fun trackOnAvatarBounce()

    fun trackFacebookLoggedOut()

    fun trackVisitGithub()

    fun trackContactDetailsViewed(contact: Contact)

    fun trackNamedaysScreen()

    fun trackDailyReminderTriggered()
}
