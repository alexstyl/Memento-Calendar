package com.alexstyl.specialdates.analytics;

import com.alexstyl.specialdates.TimeOfDay;
import com.alexstyl.specialdates.donate.Donation;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

class MixPanel implements Analytics {

    private final MixpanelAPI mixpanel;

    MixPanel(MixpanelAPI mixpanel) {
        this.mixpanel = mixpanel;
    }

    @Override
    public void trackAction(Action action) {
        mixpanel.track("Action: " + action.getName());
    }

    @Override
    public void trackAction(ActionWithParameters event) {
        JSONObject properties = createJSONfor(event);
        mixpanel.track("Action: " + event.getName(), properties);

    }

    @Override
    public void trackScreen(Screen screen) {
        mixpanel.track("ScreenView: " + screen.screenName());
    }

    @Override
    public void trackAddEventsCancelled() {
        mixpanel.track("add events cancelled");
    }

    @Override
    public void trackEventAddedSuccessfully() {
        mixpanel.track("add events success");
    }

    @Override
    public void trackContactSelected() {
        mixpanel.track("contact selected");
    }

    @Override
    public void trackEventDatePicked(EventType eventType) {
        JSONObject properties = createPropertyFor(eventType);
        mixpanel.track("event date picked ", properties);
    }

    @Override
    public void trackEventRemoved(EventType eventType) {
        JSONObject properties = createPropertyFor(eventType);
        mixpanel.track("event removed", properties);
    }

    @Override
    public void trackImageCaptured() {
        mixpanel.track("image captured");
    }

    @Override
    public void trackExistingImagePicked() {
        mixpanel.track("existing image picked");
    }

    @Override
    public void trackAvatarSelected() {
        mixpanel.track("avatar selected");
    }

    @Override
    public void trackContactUpdated() {
        mixpanel.track("contact updated");
    }

    @Override
    public void trackContactCreated() {
        mixpanel.track("contact created");
    }

    @Override
    public void trackDailyReminderEnabled() {
        mixpanel.track("daily reminder enabled");
    }

    @Override
    public void trackDailyReminderDisabled() {
        mixpanel.track("daily reminder disabled");
    }

    @Override
    public void trackDailyReminderTimeUpdated(TimeOfDay timeOfDay) {
        JSONObject properties = createPropertyFor(timeOfDay);
        mixpanel.track("daily reminder time updated", properties);
    }

    @Override
    public void trackWidgetAdded(Widget widget) {
        mixpanel.track("widget_added", widgetNameOf(widget));
    }

    private static JSONObject widgetNameOf(Widget widget) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("widget_name", widget.getWidgetName());
            return properties;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public void trackWidgetRemoved(Widget widget) {
        mixpanel.track("widget_removed", widgetNameOf(widget));
    }

    @Override
    public void trackDonationStarted(Donation donation) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("id", donation.getIdentifier());
            properties.put("amount", donation.getAmount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("donation started", properties);
    }

    @Override
    public void trackAppInviteRequested() {
        mixpanel.track("app_invite_requested");
    }

    private static JSONObject createJSONfor(ActionWithParameters event) {
        JSONObject properties = new JSONObject();
        try {
            properties.put(event.getLabel(), event.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static JSONObject createPropertyFor(EventType eventType) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("event type", eventType.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private JSONObject createPropertyFor(TimeOfDay timeOfDay) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("time", timeOfDay.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
