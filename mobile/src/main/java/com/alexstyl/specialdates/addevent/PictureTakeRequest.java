package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

final class PictureTakeRequest {

    private static final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private final PackageManager packageManager;

    PictureTakeRequest(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    void requestPicture(Activity activity, int code) {
        activity.startActivityForResult(takePictureIntent, code);
    }

    boolean canTakePicture() {
        return takePictureIntent.resolveActivity(packageManager) != null;
    }
}
