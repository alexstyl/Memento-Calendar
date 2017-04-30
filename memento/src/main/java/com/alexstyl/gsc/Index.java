package com.alexstyl.gsc;

public class Index {

    private static final int NOT_STARTED = -1;
    private static final int ENDED = -2;

    private int index = NOT_STARTED;
    private int length;

    public Index(int length) {
        this.length = length;
        if (length == 0) {
            index = ENDED;
        }

    }

    public boolean hasEnded() {
        return index == ENDED;
    }

    public int intValue() {
        return index;
    }

    public void stepUp() {
        if (hasEnded()) {
            throw new IllegalStateException("Tried to step up even when Index has ended");
        }
        ++index;

    }

    public void stepDown() {
        if (hasNotStarted()) {
            throw new IllegalStateException("Tried to step down even when Index has not started");

        }
        --index;
    }

    public boolean hasNotStarted() {
        return index == NOT_STARTED;
    }

    public void end() {
        index = ENDED;
    }

    public void setTo(int position) {
        index = position;
        if (index > this.length) {
            this.end();
        } else if (index < 0) {
            index = NOT_STARTED;
        }

    }

    @Override
    public String toString() {
        return hasNotStarted() ? "NOT STARTED" : (index == ENDED ? "ENDED" : String.valueOf(index));
    }
}
