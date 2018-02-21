package com.alexstyl.specialdates.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class BackKeyEditText extends android.support.v7.widget.AppCompatEditText {
    public BackKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnBackKeyPressedListener listener;

    public void setOnBackKeyPressedListener(OnBackKeyPressedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && listener.onBackButtonPressed()) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
