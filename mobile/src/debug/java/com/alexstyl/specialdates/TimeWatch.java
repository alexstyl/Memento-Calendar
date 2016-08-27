package com.alexstyl.specialdates;

import android.util.Log;

import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeWatch {

    private final HashMap<String, Long> events = new HashMap<>();
    private final List<Tick> ticks = new ArrayList<>();

    private final long timeCreated;

    private final String timerLabel;
    private final int threshold = -1;

    public TimeWatch(String timerLabel) {
        this.timerLabel = timerLabel;
        this.timeCreated = now();
    }

    public TimeWatch(String timerLabel, int threshold) {
        this.timerLabel = timerLabel;
        this.timeCreated = threshold;
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    public void startEvent(String eventName) {
        events.put(eventName, now());
    }

    public void stopEvent(String eventName) {
        Long time = events.remove(eventName);
        if (time != null) {
            Tick tick = new Tick(eventName, now() - time);

            appendTick(tick);
        }

    }

    private void appendTick(Tick tick) {
        throwExceptionIfOutOfThreshold(tick);
        ticks.add(tick);
    }

    public void tickStrictly(String eventLabel, int threshold) {
        if (ticks.isEmpty()) {
            Tick tick = new Tick(eventLabel, now() - timeCreated);
            throwExceptionIfOutOfThreshold(tick, threshold);
            appendTick(tick);
        } else {
            Tick previousTick = ticks.get(ticks.size() - 1);
            Tick tick = new Tick(eventLabel, now() - previousTick.getTimeCreated());
            throwExceptionIfOutOfThreshold(tick, threshold);
            appendTick(tick);
        }
    }

    public void tick(String eventLabel) {
        if (ticks.isEmpty()) {
            Tick tick = new Tick(eventLabel, now() - timeCreated);

            appendTick(tick);
        } else {
            Tick previousTick = ticks.get(ticks.size() - 1);
            Tick tick = new Tick(eventLabel, now() - previousTick.getTimeCreated());

            appendTick(tick);
        }
    }


    private void throwExceptionIfOutOfThreshold(Tick tick) {
        throwExceptionIfOutOfThreshold(tick, threshold);
    }

    private void throwExceptionIfOutOfThreshold(Tick tick, int threshold) {
        if (threshold != -1 && tick.getDuration() > threshold) {
            throw new DeveloperError(tick.getName() + " lasted more than " + threshold + "ms (" + tick.duration + ")");
        }
    }

    public void reset() {
        ticks.clear();
        events.clear();
    }

    public void dumpAll() {
        for (Tick tick : ticks) {
            Log.d(timerLabel, tick.getName() + " lasted [" + tick.getDuration() + "ms]");
        }
    }

    public void trackEvent(String label, Runnable runnable, int threshold) {
        long start = now();
        runnable.run();
        long lastedFor = now() - start;
        Tick tick = new Tick(label, lastedFor);
        throwExceptionIfOutOfThreshold(tick, threshold);
        appendTick(tick);
    }


    public void print(String label) {
        for (Tick tick : ticks) {
            if (label != null && label.equals(tick.getName())) {
                Log.d(timerLabel, tick.getName() + " lasted [" + tick.getDuration() + "ms]");
                return;
            }
        }
    }

    public void logTimeTillNow() {
        long timePassed = now() - timeCreated;
        Log.d(timerLabel, "Time Passed: " + timePassed);
    }


    private class Tick {

        private final String log;
        private final long duration;

        private final long timeCreated;

        public Tick(String log, long duration) {
            this.log = log;
            this.duration = duration;
            timeCreated = now();
        }

        public long getDuration() {
            return duration;
        }

        public String getName() {
            return log;
        }

        public String getLog() {
            return log;
        }

        public long getTimeCreated() {
            return timeCreated;
        }
    }
}
