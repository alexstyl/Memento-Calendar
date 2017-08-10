package com.alexstyl.specialdates.ui.base;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.alexstyl.specialdates.MementoApplication;

public class MementoDialog extends DialogFragment {

    protected ContentResolver getContentResolver() {
        return getActivity().getContentResolver();
    }

    protected Context getThemedContext() {
        return getActivity();
    }

    protected MementoApplication getApplication() {
        return ((MementoApplication) getActivity().getApplication());
    }
}
