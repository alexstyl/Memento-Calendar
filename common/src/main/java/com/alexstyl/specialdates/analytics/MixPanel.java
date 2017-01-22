package com.alexstyl.specialdates.analytics;

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
}
