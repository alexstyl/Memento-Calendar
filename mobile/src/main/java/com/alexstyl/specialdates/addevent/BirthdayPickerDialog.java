package com.alexstyl.specialdates.addevent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.BirthdayDatePicker;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.alexstyl.specialdates.util.ContactEventDateParser;
import com.novoda.notils.caster.Views;

public class BirthdayPickerDialog extends MementoDialog {

    public static final String TAG = "fm_tag:birthday";
    private static final String KEY_DATE = "key:birthday";
    private ContactEventDateParser parser = new ContactEventDateParser();

    private OnBirthdaySelectedListener listener;
    private BirthdayDatePicker datePicker;

    private Optional<Date> initialBirthday;

    public static BirthdayPickerDialog createDialogFor(Date birthday, OnBirthdaySelectedListener listener) {
        Bundle args = new Bundle();
        args.putString(KEY_DATE, birthday.toString());
        BirthdayPickerDialog fragment = new BirthdayPickerDialog();
        fragment.setArguments(args);
        fragment.setOnBirthdaySetListener(listener);
        return fragment;
    }

    public static BirthdayPickerDialog createDialog(OnBirthdaySelectedListener listener) {
        BirthdayPickerDialog fragment = new BirthdayPickerDialog();
        fragment.setOnBirthdaySetListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialBirthday = extractBirthday(getArguments());
    }

    private Optional<Date> extractBirthday(Bundle arguments) {
        if (arguments == null || !arguments.containsKey(KEY_DATE)) {
            return Optional.absent();
        } else {
            String birthday = arguments.getString(KEY_DATE);
            return extractFrom(birthday);
        }
    }

    private Optional<Date> extractFrom(String birthday) {
        try {
            Date parsedDate = parser.parse(birthday);
            if (parsedDate.hasYear()) {
                return new Optional<>(Date.on(parsedDate.getDayOfMonth(), parsedDate.getMonth(), parsedDate.getYear()));
            } else {
                return new Optional<>(Date.on(parsedDate.getDayOfMonth(), parsedDate.getMonth()));
            }
        } catch (DateParseException e) {
            ErrorTracker.track(e);
            return Optional.absent();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getThemedContext()).inflate(R.layout.dialog_birthday_picker, null, false);
        datePicker = Views.findById(view, R.id.dialog_birthday_picker);
        if (initialBirthday.isPresent()) {
            datePicker.setDisplayingDate(initialBirthday.get());
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.birthday_picker_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.birthday_picker_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onBirthdaySet(datePicker.getDisplayingBirthday());
                    }
                })
                .create();
    }

    private void setOnBirthdaySetListener(OnBirthdaySelectedListener listener) {
        this.listener = listener;
    }

    public static void resetListenerToDialog(FragmentManager fragmentManager, OnBirthdaySelectedListener onBirthdaySelectedListener) {
        BirthdayPickerDialog dialog = (BirthdayPickerDialog) fragmentManager.findFragmentByTag(TAG);
        if (dialog != null) {
            dialog.setOnBirthdaySetListener(onBirthdaySelectedListener);
        }
    }

    public interface OnBirthdaySelectedListener {
        void onBirthdaySet(Date birthday);
    }
}
