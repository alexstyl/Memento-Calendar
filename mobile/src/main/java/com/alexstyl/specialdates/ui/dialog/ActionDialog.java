
package com.alexstyl.specialdates.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.datedetails.SimpleDataAdapter;
import com.alexstyl.specialdates.entity.DataType;
import com.alexstyl.specialdates.entity.Email;
import com.alexstyl.specialdates.entity.Phone;
import com.alexstyl.specialdates.ui.base.MementoDialog;

import java.util.ArrayList;

public class ActionDialog extends MementoDialog {

    protected static final String ARG_DATA = BuildConfig.APPLICATION_ID + ".ARG_CONTACT";
    private static final String ARG_DATATYPE = BuildConfig.APPLICATION_ID + ".ARG_DATATYPE";

    /**
     * Creates a new instance of the ActionDialog fragment.
     */
    public static ActionDialog newPhoneInstance(final ArrayList<Phone> phones) {
        final ActionDialog dialog = newInstance(phones, DataType.TYPE_PHONE);
        dialog.setOnActionPerformedListener(
                new OnActionPerformedListener() {

                    @Override
                    public void onActionPerformed(int o) {
                        try {
                            phones.get(o).dial(dialog.getActivity());
                        } catch (Exception e) {
                            ErrorTracker.track(e);
                            Toast.makeText(dialog.getActivity(), R.string.no_app_found, Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                }
        );
        return dialog;
    }

    public static ActionDialog newSMSInstance(final ArrayList<Phone> phones) {
        final ActionDialog dialog = newInstance(phones, DataType.TYPE_PHONE);
        dialog.setOnActionPerformedListener(
                new OnActionPerformedListener() {

                    @Override
                    public void onActionPerformed(int o) {
                        try {
                            phones.get(o).sendText(dialog.getActivity());
                        } catch (Exception e) {
                            ErrorTracker.track(e);
                            Toast.makeText(dialog.getActivity(), R.string.no_app_found, Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                }
        );
        return dialog;
    }

    public static ActionDialog newEmailInstance(final ArrayList<Email> emails) {
        final ActionDialog dialog = newInstance(emails, DataType.TYPE_EMAIL);
        dialog.setOnActionPerformedListener(
                new OnActionPerformedListener() {

                    @Override
                    public void onActionPerformed(int o) {
                        try {
                            emails.get(o).sendMail(dialog.getActivity());
                        } catch (Exception e) {
                            ErrorTracker.track(e);
                            Toast.makeText(dialog.getActivity(), R.string.no_app_found, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        return dialog;
    }

    /**
     * @param data        The list containing {@link Email}s or {@link Phone}s
     * @param displayType The type of data to display. See
     *                    {@link DataType#TYPE_PHONE}
     * @return
     */
    private static ActionDialog newInstance(ArrayList<? extends DataType> data,
                                            int displayType) {
        ActionDialog dialog = new ActionDialog();
        if (data != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_DATA, data);
            bundle.putInt(ARG_DATATYPE, displayType);
            dialog.setArguments(bundle);
        }
        dialog.setCancelable(true);
        return dialog;
    }

    public interface OnActionPerformedListener {
        void onActionPerformed(int o);
    }

    private OnActionPerformedListener l;

    public void setOnActionPerformedListener(OnActionPerformedListener l) {
        this.l = l;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int dataType = getArguments().getInt(ARG_DATATYPE);
        String title = getString(R.string.choose) + " ";
        if (dataType == DataType.TYPE_PHONE) {
            title = title + getString(R.string.number);
        } else if (dataType == DataType.TYPE_EMAIL) {
            title = title + getString(R.string.email);
        }

        ArrayList<DataType> dataz = getArguments().getParcelableArrayList(ARG_DATA);
        SimpleDataAdapter adapter = new SimpleDataAdapter(getActivity(), dataz);
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setAdapter(
                        adapter, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                l.onActionPerformed(which);

                            }
                        }
                )
                .create();
    }
}
