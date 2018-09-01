package com.alexstyl.specialdates.analytics

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Size
import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.donate.Donation
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsImpl(private val firebase: FirebaseAnalytics) : Analytics {

    override fun trackThemeSelected(string: String) {
        firebase.logEvent(Action.SELECT_THEME.name, Bundle().apply {
            putString("theme name", string)
        })
    }

    override fun trackScreen(screen: Screen) {
        val bundle = Bundle()
        bundle.putString("screen_name", screen.screenName())
        firebase.logEvent("screen_view", bundle)
    }

    override fun trackAddEventsCancelled() {
        firebase.logEvent("add_events_cancelled")
    }

    override fun trackEventAddedSuccessfully() {
        firebase.logEvent("add_events_success")
    }

    override fun trackContactSelected() {
        firebase.logEvent("contact_selected")
    }

    override fun trackEventDatePicked(eventType: EventType) {
        val properties = createPropertyFor(eventType)
        firebase.logEvent("event_date_picked", properties)
    }

    override fun trackEventRemoved(eventType: EventType) {
        val properties = createPropertyFor(eventType)
        firebase.logEvent("event_removed", properties)
    }

    override fun trackImageCaptured() {
        firebase.logEvent("image_captured")
    }

    override fun trackExistingImagePicked() {
        firebase.logEvent("existing_image_picked")
    }

    override fun trackAvatarSelected() {
        firebase.logEvent("avatar_selected")
    }

    override fun trackContactUpdated() {
        firebase.logEvent("contact_updated")
    }

    override fun trackContactCreated() {
        firebase.logEvent("contact_created")
    }

    override fun trackDailyReminderEnabled() {
        firebase.logEvent("daily_reminder_enabled")
    }

    override fun trackDailyReminderDisabled() {
        firebase.logEvent("daily_reminder_disabled")
    }

    override fun trackDailyReminderTimeUpdated(timeOfDay: TimeOfDay) {
        val properties = createPropertyFor(timeOfDay)
        firebase.logEvent("daily_reminder_time_updated", properties)
    }

    override fun trackWidgetAdded(widget: Widget) {
        firebase.logEvent("widget_added", widgetNameOf(widget))
    }

    private fun widgetNameOf(widget: Widget): Bundle {
        return Bundle().apply {
            putString("widget_name", widget.widgetName)
        }
    }

    override fun trackWidgetRemoved(widget: Widget) {
        firebase.logEvent("widget_removed", widgetNameOf(widget))
    }

    override fun trackDonationStarted(donation: Donation) {
        val properties = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, donation.identifier)
            putString(FirebaseAnalytics.Param.PRICE, donation.amount)
        }
        firebase.logEvent("donation_started", properties)
    }

    override fun trackAppInviteRequested() {
        firebase.logEvent("app_invite_requested")
    }

    override fun trackDonationRestored() {
        firebase.logEvent("donation_restored")
    }

    override fun trackDonationPlaced(donation: Donation) {
        val properties = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, donation.identifier)
            putString(FirebaseAnalytics.Param.PRICE, donation.amount)
        }
        firebase.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, properties)
    }

    override fun trackFacebookLoggedIn() {
        firebase.logEvent("facebook_log_in")
    }

    override fun trackOnAvatarBounce() {
        firebase.logEvent("avatar_bounce")
    }

    override fun trackFacebookLoggedOut() {
        firebase.logEvent("facebook_log_out")
    }

    override fun trackVisitGithub() {
        firebase.logEvent("visit_github")
    }

    override fun trackContactDetailsViewed(contact: Contact) {
        firebase.logEvent("view_contact_details")
    }

    override fun trackNamedaysScreen() {
        firebase.logEvent("namedays_screen")
    }

    private fun createPropertyFor(eventType: EventType): Bundle {
        val properties = Bundle()
        properties.putInt("event_type", eventType.id)
        return properties
    }

    private fun createPropertyFor(timeOfDay: TimeOfDay): Bundle {
        return Bundle().apply {
            putString("time", timeOfDay.toString())
        }
    }

    private fun FirebaseAnalytics.logEvent(@NonNull @Size(min = 1L, max = 40L) eventName: String) {
        return logEvent(eventName, Bundle.EMPTY)
    }

    override fun trackDailyReminderTriggered() {
        firebase.logEvent("daily reminder triggered")
    }
}

