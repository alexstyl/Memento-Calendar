package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

final class ToastDisplayer implements MessageDisplayer {

    private final Context context;

    ToastDisplayer(Context context) {
        this.context = context;
    }

    @Override
    public void showMessage(@StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }
}
