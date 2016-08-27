package com.alexstyl.specialdates.util;

import android.os.AsyncTask;

public abstract class AsynchronousTask<T> {

    public abstract T performTask();

    protected abstract void onTaskCompleted(T accounts);

    private class Task extends AsyncTask<Void, Void, T> {

        @Override
        protected T doInBackground(Void... params) {
            return performTask();
        }

        @Override
        protected void onPostExecute(T t) {
            onTaskCompleted(t);

        }

    }

    public void start() {
        new Task().execute();
    }
}
