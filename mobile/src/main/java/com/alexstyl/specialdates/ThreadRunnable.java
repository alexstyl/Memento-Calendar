package com.alexstyl.specialdates;

public class ThreadRunnable {

    public void start(Runnable runnable) {
        new Thread(runnable).start();
    }
}
