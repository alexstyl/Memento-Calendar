package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.novoda.notils.caster.Classes;

final public class PictureOptionSelectDialog extends MementoDialog {

    private static final String KEY_INCLUDE_REMOVE_OPTION = "key:remove_option";

    private OnPictureOptionSelectedListener listener;

    interface OnPictureOptionSelectedListener {
        void onOptionSelected(PictureSelectOption option);

    }

    static PictureOptionSelectDialog withoutRemoveOption() {
        return createDialog(false);
    }

    static PictureOptionSelectDialog withRemoveOption() {
        return createDialog(true);
    }

    private static PictureOptionSelectDialog createDialog(boolean includeRemoveOption) {
        Bundle args = new Bundle(1);
        args.putBoolean(KEY_INCLUDE_REMOVE_OPTION, includeRemoveOption);
        PictureOptionSelectDialog fragment = new PictureOptionSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = Classes.from(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final PictureSelectAdapter adapter = new PictureSelectAdapter(optionsToDisplay());
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_event_pick_image_title)
                .setAdapter(
                        adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onOptionSelected(adapter.getItem(which));
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private PictureSelectOption[] optionsToDisplay() {
        if (includesRemoveOption()) {
            return PictureSelectOption.values();
        } else {
            PictureSelectOption[] options = new PictureSelectOption[2];
            options[0] = PictureSelectOption.TAKE_PICTURE;
            options[1] = PictureSelectOption.PICK_EXISTING;
            return options;
        }
    }

    private boolean includesRemoveOption() {
        return getArguments().getBoolean(KEY_INCLUDE_REMOVE_OPTION, false);
    }

}
