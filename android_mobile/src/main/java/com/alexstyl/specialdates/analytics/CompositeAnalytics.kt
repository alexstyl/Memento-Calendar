package com.alexstyl.specialdates.analytics

import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.donate.Donation
import com.alexstyl.specialdates.events.peopleevents.EventType

class CompositeAnalytics(private vararg val analytics: Analytics) : Analytics {
    override fun trackThemeSelected(string: String) {
        analytics.forEach {
            it.trackThemeSelected(string)
        }
    }

    override fun trackScreen(screen: Screen) {
        analytics.forEach {
            it.trackScreen(screen)
        }
    }

    override fun trackAddEventsCancelled() {
        analytics.forEach {
            it.trackAddEventsCancelled()
        }
    }

    override fun trackEventAddedSuccessfully() {
        analytics.forEach {
            it.trackEventAddedSuccessfully()
        }
    }

    override fun trackContactSelected() {
        analytics.forEach {
            it.trackContactSelected()
        }
    }

    override fun trackEventDatePicked(eventType: EventType) {
        analytics.forEach {
            it.trackEventDatePicked(eventType)
        }
    }

    override fun trackEventRemoved(eventType: EventType) {
        analytics.forEach {
            it.trackEventRemoved(eventType)
        }
    }

    override fun trackImageCaptured() {
        analytics.forEach {
            it.trackImageCaptured()
        }
    }

    override fun trackExistingImagePicked() {
        analytics.forEach {
            it.trackExistingImagePicked()
        }
    }

    override fun trackAvatarSelected() {
        analytics.forEach {
            it.trackAvatarSelected()
        }
    }

    override fun trackContactUpdated() {
        analytics.forEach {
            it.trackContactUpdated()
        }
    }

    override fun trackContactCreated() {
        analytics.forEach {
            it.trackContactCreated()
        }
    }

    override fun trackDailyReminderEnabled() {
        analytics.forEach {
            it.trackDailyReminderEnabled()
        }
    }

    override fun trackDailyReminderDisabled() {
        analytics.forEach {
            it.trackDailyReminderDisabled()
        }
    }

    override fun trackDailyReminderTimeUpdated(timeOfDay: TimeOfDay) {
        analytics.forEach {
            it.trackDailyReminderTimeUpdated(timeOfDay)
        }
    }

    override fun trackWidgetAdded(widget: Widget) {
        analytics.forEach {
            it.trackWidgetAdded(widget)
        }
    }

    override fun trackWidgetRemoved(widget: Widget) {
        analytics.forEach {
            it.trackWidgetRemoved(widget)
        }
    }

    override fun trackDonationStarted(donation: Donation) {
        analytics.forEach {
            it.trackDonationStarted(donation)
        }
    }

    override fun trackAppInviteRequested() {
        analytics.forEach {
            it.trackAppInviteRequested()
        }
    }

    override fun trackDonationRestored() {
        analytics.forEach {
            it.trackDonationRestored()
        }
    }

    override fun trackDonationPlaced(donation: Donation) {
        analytics.forEach {
            it.trackDonationPlaced(donation)
        }
    }

    override fun trackFacebookLoggedIn() {
        analytics.forEach {
            it.trackFacebookLoggedIn()
        }
    }

    override fun trackOnAvatarBounce() {
        analytics.forEach {
            it.trackOnAvatarBounce()
        }
    }

    override fun trackFacebookLoggedOut() {
        analytics.forEach {
            it.trackFacebookLoggedOut()
        }
    }

    override fun trackVisitGithub() {
        analytics.forEach {
            it.trackVisitGithub()
        }
    }

    override fun trackContactDetailsViewed(contact: Contact) {
        analytics.forEach {
            it.trackContactDetailsViewed(contact)
        }
    }

    override fun trackNamedaysScreen() {
        analytics.forEach {
            it.trackNamedaysScreen()
        }
    }

}
