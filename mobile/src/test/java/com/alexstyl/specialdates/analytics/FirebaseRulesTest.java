package com.alexstyl.specialdates.analytics;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.fail;

/**
 * Class that tests our business logic against all Firebase documentation rules
 *
 * @see <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event">FirebaseAnalytics.Event documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class FirebaseRulesTest {

    private final static int MAX_EVENT_NAME_CHAR_COUNT = 32;
    private final static int MAX_EVENTS_COUNT = 500;

    @Mock
    private Analytics mockAnalytics;

    @Test
    public void actionUpTo32characterlong() {
        for (Action action : Action.values()) {
            if (action.getName().length() > MAX_EVENT_NAME_CHAR_COUNT) {
                failFor(action);
            }
        }
    }

    @Test
    public void actionWithParamsUpTo32CharacterLong() {
        List<String> labels = Arrays.asList("enabled", "select", "success", "source");
        List<String> values = Arrays.asList("true", "false", "success", "external", "sms", "call");

        for (Action action : Action.values()) {
            for (String label : labels) {
                for (String value : values) {
                    ActionWithParameters actionWithParameters = new ActionWithParameters(action, label, value);
                    String event = Firebase.format(actionWithParameters);
                    if (event.length() > MAX_EVENT_NAME_CHAR_COUNT) {
                        failFor(actionWithParameters);
                    }

                }

            }
        }
    }

    @Test
    public void doesNotExceedNumberOfAllowedEvents() {
        int numberOfEventsUsed = Action.values().length;
        if (numberOfEventsUsed > MAX_EVENTS_COUNT) {
            fail("You can only use up to " + MAX_EVENTS_COUNT + " unique Firebase events. Currently using " + numberOfEventsUsed);
        }

    }

    private static void failFor(Action action) {
        int characterCount = action.getName().length();
        fail(action + " is not a correct action to be used. It contains more than " + MAX_EVENT_NAME_CHAR_COUNT + " characters. Has " + characterCount);
    }

    private static void failFor(ActionWithParameters action) {
        int characterCount = Firebase.format(action).length();
        String message = action + " is not a correct action to be used. It contains more than " + MAX_EVENT_NAME_CHAR_COUNT + " characters. Has " + characterCount;
        fail(message);
    }

}
