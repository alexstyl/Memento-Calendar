package com.alexstyl.specialdates.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.alexstyl.specialdates.ui.base.MementoDialog;

/**
 * FragmentDialog displaying a ProgressDialog
 * <p>Created by Alex on 6/11/2014.
 * </p>
 */
public class ProgressFragmentDialog extends MementoDialog {

    private static final String ARG_MESSAGE = "alexstyl:message";
    private static final String ARG_ONE_SHOT = "alexstyl:one_shot";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        ProgressDialog dialog = new ProgressDialog(getActivity());
            String message = getArguments().getString(ARG_MESSAGE);
            if (!TextUtils.isEmpty(message)) {
                dialog.setMessage(message);
        }
        return dialog;
    }

    public static ProgressFragmentDialog newInstance(String message, boolean oneShot) {
        ProgressFragmentDialog dialog = new ProgressFragmentDialog();
        Bundle args = new Bundle(2);
        args.putBoolean(ARG_ONE_SHOT, oneShot);
        if (!TextUtils.isEmpty(message)) {
            args.putString(ARG_MESSAGE, message);
        }
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getArguments().getBoolean(ARG_ONE_SHOT)) {
            dismissAllowingStateLoss();
        }
    }
}
