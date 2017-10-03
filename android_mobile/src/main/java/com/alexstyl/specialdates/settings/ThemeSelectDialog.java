package com.alexstyl.specialdates.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.ui.base.MementoDialog;

public class ThemeSelectDialog extends MementoDialog {

    private OnThemeSelectedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ThemeSelectAdapter adapter = new ThemeSelectAdapter(new MementoThemeNameComparator(getResources()));
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.theme_preference_title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onThemeSelected(adapter.getItem(which));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    public void setOnThemeSelectedListener(OnThemeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnThemeSelectedListener {
        void onThemeSelected(MementoTheme theme);
    }
}
