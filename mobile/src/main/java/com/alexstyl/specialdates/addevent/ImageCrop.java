package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.DisplayPhoto;

import com.alexstyl.specialdates.MementoApplication;

final class ImageCrop {

    private static final String ACTION_CROP = "com.android.camera.action.CROP";
    private static final int MAX_RESOLUTION = 720;

    static Intent newIntent(Uri pathToImageToCrop) {
        int size = queryCropSize();
        Intent intent = new Intent(ACTION_CROP);
        intent.setData(pathToImageToCrop);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        return intent;
    }

    private static int queryCropSize() {
        Context context = MementoApplication.getContext();
        Cursor cursor = context.getContentResolver().query(
                DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI,
                new String[]{DisplayPhoto.DISPLAY_MAX_DIM}, null, null, null
        );
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return MAX_RESOLUTION;
    }
}
