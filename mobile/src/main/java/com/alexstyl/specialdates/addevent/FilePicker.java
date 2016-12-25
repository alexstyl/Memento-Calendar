package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.content.Intent;

final class FilePicker {

    void pickAFile(Activity activity, int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, code);
    }
}
