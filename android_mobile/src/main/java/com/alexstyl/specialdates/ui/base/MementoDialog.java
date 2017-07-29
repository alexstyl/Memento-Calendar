package com.alexstyl.specialdates.ui.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.alexstyl.specialdates.MementoApplication;
import com.novoda.notils.caster.Classes;

public class MementoDialog extends DialogFragment {

    private MementoActivity activity;

    public MementoActivity getMementoActivity() {
        return activity;
    }

    @Override
    public void onAttach(Activity activity) {
        activity = Classes.from(activity);
        super.onAttach(activity);
    }

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
