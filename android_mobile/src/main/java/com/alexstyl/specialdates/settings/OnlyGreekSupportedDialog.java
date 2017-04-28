package com.alexstyl.specialdates.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.MementoDialog;

public class OnlyGreekSupportedDialog extends MementoDialog {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.bankholidays_preferences_only_greek_supported_message)
                .setPositiveButton(android.R.string.ok, null).create();
    }
}
