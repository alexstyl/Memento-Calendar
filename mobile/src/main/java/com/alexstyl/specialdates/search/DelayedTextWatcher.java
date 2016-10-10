package com.alexstyl.specialdates.search;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

class DelayedTextWatcher implements TextWatcher {

    private static final long SHORT_DELAY = 50;

    private final TextUpdatedCallback textWatchTextUpdatedCallback;
    private final Handler handler;
    private String text;

    public static DelayedTextWatcher newInstance(TextUpdatedCallback textUpdatedCallback) {
        Handler handler = new Handler();
        return new DelayedTextWatcher(textUpdatedCallback, handler);
    }

    private DelayedTextWatcher(TextUpdatedCallback textWatchTextUpdatedCallback, Handler handler) {
        this.textWatchTextUpdatedCallback = textWatchTextUpdatedCallback;
        this.handler = handler;
    }

    private final Runnable timeEndRunnable = new Runnable() {
        @Override
        public void run() {
            if (text.length() > 0) {
                textWatchTextUpdatedCallback.onTextConfirmed(text);
            } else {
                textWatchTextUpdatedCallback.onEmptyTextConfirmed();
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        handler.removeCallbacks(timeEndRunnable);
        handler.postDelayed(timeEndRunnable, SHORT_DELAY);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        text = s.toString().trim();
        textWatchTextUpdatedCallback.onTextChanged(text);
    }

    public interface TextUpdatedCallback {
        void onTextChanged(String text);

        void onEmptyTextConfirmed();

        void onTextConfirmed(String text);
    }

}
