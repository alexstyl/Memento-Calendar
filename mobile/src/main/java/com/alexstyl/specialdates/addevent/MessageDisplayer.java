package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

final class MessageDisplayer {

    private final Context context;

    MessageDisplayer(Context context) {
        this.context = context;
    }

    void showMessage(@StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }
}
