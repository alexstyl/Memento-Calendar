package com.alexstyl.specialdates.addevent;

import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;

enum PictureSelectOption {
    REMOVE(0, R.string.add_event_remove_photo),
    TAKE_PICTURE(1, R.string.add_event_take_photo),
    PICK_EXISTING(2, R.string.add_event_pick_from_files);

    private final int index;
    private final int label;

    PictureSelectOption(int index, int label) {
        this.index = index;
        this.label = label;
    }

    int getIndex() {
        return index;
    }

    public static boolean isRemovePhoto(int which, boolean includesRemoveOption) {
        if (!includesRemoveOption) {
            throw new IllegalStateException("Remove Option is not included. It is impossible for it to have an Index");
        }
        return which == REMOVE.index;
    }

    public static boolean isTakePicture(int which, boolean includesRemoveOption) {
        return includesRemoveOption ? which == REMOVE.index + 1 : which == REMOVE.index;
    }

    public static boolean isPickFromFile(int which, boolean includesRemoveOption) {
        return includesRemoveOption ? which == PICK_EXISTING.index + 1 : which == PICK_EXISTING.index;
    }

    @StringRes
    public int getLabel() {
        return label;
    }
}
