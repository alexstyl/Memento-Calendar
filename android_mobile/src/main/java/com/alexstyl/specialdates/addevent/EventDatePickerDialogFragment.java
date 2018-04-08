package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.EventDatePicker;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.alexstyl.specialdates.date.DateParser;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;

public class EventDatePickerDialogFragment extends MementoDialog {

    private static final String KEY_DATE = "key:date";
    private static final String ARG_EVENT_TYPE_ID = "arg:event_type_id";

    private OnEventDatePickedListener listener;
    private EventDatePicker datePicker;

    private Optional<Date> initialDate;
    @Inject Strings strings;
    @Inject DateParser dateParser;

    public static EventDatePickerDialogFragment newInstance(EventType eventType, Optional<Date> date) {
        EventDatePickerDialogFragment dialogFragment = new EventDatePickerDialogFragment();
        Bundle args = new Bundle(2);
        args.putInt(ARG_EVENT_TYPE_ID, eventType.getId());
        if (date.isPresent()) {
            String label = ShortDateLabelCreator.INSTANCE.createLabelWithYearPreferredFor(date.get());
            args.putString(KEY_DATE, label);
        }
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = Classes.from(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppComponent applicationModule = getApplication().getApplicationModule();
        applicationModule.inject(this);
        initialDate = getDate();
    }

    private Optional<Date> getDate() {
        Bundle arguments = getArguments();
        if (arguments.containsKey(KEY_DATE)) {
            String birthday = arguments.getString(KEY_DATE);
            return parseFrom(birthday);
        } else {
            return Optional.Companion.absent();
        }
    }

    private Optional<Date> parseFrom(String birthday) {
        try {
            Date parsedDate = dateParser.parse(birthday);
            return new Optional<>(parsedDate);
        } catch (DateParseException e) {
            e.printStackTrace();
            return Optional.Companion.absent();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getThemedContext()).inflate(R.layout.dialog_birthday_picker, null, false);
        datePicker = Views.findById(view, R.id.dialog_birthday_picker);
        final EventType eventType = getEventType();

        if (initialDate.isPresent()) {
            datePicker.setDisplayingDate(initialDate.get());
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(eventType.getEventName(strings))
                .setView(view)
                .setPositiveButton(R.string.birthday_picker_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = datePicker.getDisplayingDate();
                        listener.onDatePicked(eventType, date);
                    }
                })
                .create();
    }

    public EventType getEventType() {
        @EventTypeId int eventTypeId = getArguments().getInt(ARG_EVENT_TYPE_ID);
        return StandardEventType.fromId(eventTypeId);
    }

    public interface OnEventDatePickedListener {
        void onDatePicked(EventType eventType, Date date);
    }
}
