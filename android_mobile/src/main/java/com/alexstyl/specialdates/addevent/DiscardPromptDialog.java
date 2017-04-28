package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.novoda.notils.caster.Classes;

public class DiscardPromptDialog extends MementoDialog {

    interface Listener {
        void onDiscardChangesSelected();
    }

    private Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = Classes.from(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_event_discard_changes_title)
                .setPositiveButton(R.string.add_event_discard_changes_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDiscardChangesSelected();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }
}
