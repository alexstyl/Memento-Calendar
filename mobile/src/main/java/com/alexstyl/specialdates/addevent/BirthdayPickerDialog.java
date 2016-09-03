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

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.BirthdayDatePicker;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.caster.Views;
import com.novoda.notils.exception.DeveloperError;

public class BirthdayPickerDialog extends MementoDialog {

    public static final String TAG = "fm_tag:birthday";
    private static final String KEY_DATE = "key:birthday";
    private DateParser parser = new DateParser();

    private OnBirthdaySelectedListener listener;
    private BirthdayDatePicker datePicker;

    private Birthday initialBirthday;

    public static BirthdayPickerDialog createDialogFor(Birthday birthday, OnBirthdaySelectedListener listener) {
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

    private Birthday extractBirthday(Bundle arguments) {
        if (arguments == null || !arguments.containsKey(KEY_DATE)) {
            DayDate today = DayDate.today();
            return Birthday.on(today);
        } else {
            String birthday = arguments.getString(KEY_DATE);
            try {
                DayDate parsedDate = parser.parse(birthday);
                return Birthday.on(parsedDate);
            } catch (DateParseException e) {
                throw new DeveloperError("Invalid birthday to display [" + birthday + "]");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getThemedContext()).inflate(R.layout.dialog_birthday_picker, null, false);
        datePicker = Views.findById(view, R.id.dialog_birthday_picker);
        datePicker.setDisplayingDate(initialBirthday);

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
        void onBirthdaySet(Birthday birthday);
    }
}
