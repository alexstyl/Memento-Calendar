package com.alexstyl.specialdates.analytics;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MixPanel implements Analytics {

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

    private static JSONObject createJSONfor(ActionWithParameters event) {
        JSONObject properties = new JSONObject();
        try {
            properties.put(event.getLabel(), event.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
