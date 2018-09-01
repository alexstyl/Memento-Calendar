package com.alexstyl.specialdates.analytics

import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.donate.Donation
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.mixpanel.android.mpmetrics.MixpanelAPI

import org.json.JSONException
import org.json.JSONObject

class MixPanel(private val mixpanel: MixpanelAPI) : Analytics {
    override fun trackThemeSelected(string: String) {
        mixpanel.track(Action.SELECT_THEME.name, JSONObject().apply {
            put("theme name", string)
        })
    }

    override fun trackScreen(screen: Screen) {
        mixpanel.track("ScreenView: " + screen.screenName())
    }

    override fun trackAddEventsCancelled() {
        mixpanel.track("add events cancelled")
    }

    override fun trackEventAddedSuccessfully() {
        mixpanel.track("add events success")
    }

    override fun trackContactSelected() {
        mixpanel.track("contact selected")
    }

    override fun trackEventDatePicked(eventType: EventType) {
        val properties = createPropertyFor(eventType)
        mixpanel.track("event date picked ", properties)
    }

    override fun trackEventRemoved(eventType: EventType) {
        val properties = createPropertyFor(eventType)
        mixpanel.track("event removed", properties)
    }

    override fun trackImageCaptured() {
        mixpanel.track("image captured")
    }

    override fun trackExistingImagePicked() {
        mixpanel.track("existing image picked")
    }

    override fun trackAvatarSelected() {
        mixpanel.track("avatar selected")
    }

    override fun trackContactUpdated() {
        mixpanel.track("contact updated")
    }

    override fun trackContactCreated() {
        mixpanel.track("contact created")
    }

    override fun trackDailyReminderEnabled() {
        mixpanel.track("daily reminder enabled")
    }

    override fun trackDailyReminderDisabled() {
        mixpanel.track("daily reminder disabled")
    }

    override fun trackDailyReminderTimeUpdated(timeOfDay: TimeOfDay) {
        val properties = createPropertyFor(timeOfDay)
        mixpanel.track("daily reminder time updated", properties)
    }

    override fun trackWidgetAdded(widget: Widget) {
        mixpanel.track("widget_added", widgetNameOf(widget))
    }

    private fun widgetNameOf(widget: Widget): JSONObject {
        val properties = JSONObject()
        try {
            properties.put("widget_name", widget.widgetName)
            return properties
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return properties
    }

    override fun trackWidgetRemoved(widget: Widget) {
        mixpanel.track("widget_removed", widgetNameOf(widget))
    }

    override fun trackDonationStarted(donation: Donation) {
        val properties = JSONObject()
        try {
            properties.put("id", donation.identifier)
            properties.put("amount", donation.amount)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mixpanel.track("donation started", properties)
    }

    override fun trackAppInviteRequested() {
        mixpanel.track("app_invite_requested")
    }

    override fun trackDonationRestored() {
        mixpanel.track("donation_restored")
    }

    override fun trackDonationPlaced(donation: Donation) {
        val properties = JSONObject()
        try {
            properties.put("amount", donation.amount)
            properties.put("identifier", donation.identifier)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mixpanel.track("donation_placed", properties)
    }

    override fun trackFacebookLoggedIn() {
        mixpanel.track("facebook_log_in")
    }

    override fun trackOnAvatarBounce() {
        mixpanel.track("avatar bounce")
    }

    override fun trackFacebookLoggedOut() {
        mixpanel.track("facebook_log_out")
    }

    override fun trackVisitGithub() {
        mixpanel.track("visit_github")
    }

    override fun trackContactDetailsViewed(contact: Contact) {
        mixpanel.track("view_contact_details")
    }

    override fun trackNamedaysScreen() {
        mixpanel.track("namedays_screen")
    }

    override fun trackDailyReminderTriggered() {
        mixpanel.track("daily_reminder_triggered")
    }

    private fun createPropertyFor(eventType: EventType): JSONObject {
        val properties = JSONObject()
        try {
            properties.put("event type", eventType.id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return properties
    }

    private fun createPropertyFor(timeOfDay: TimeOfDay): JSONObject {
        val properties = JSONObject()
        try {
            properties.put("time", timeOfDay.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return properties
    }
}
