package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.DisplayPhoto;

import com.alexstyl.specialdates.MementoApplication;

final class ImageCrop {

    private static final String ACTION_CROP = "com.android.camera.action.CROP";
    private static final int MAX_RESOLUTION = 720;

    static Intent newIntent(Uri pathToImageToCrop) {
        int size = getSize();
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

    static Bitmap getImageFrom(Intent data) {
        Bundle extras = data.getExtras();
        return extras.getParcelable("data");
    }

    public static int getSize() {
        Context context = MementoApplication.getContext();
        Cursor c = context.getContentResolver().query(
                DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI,
                new String[]{DisplayPhoto.DISPLAY_MAX_DIM}, null, null, null
        );
        int size = 0;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    size = c.getInt(0);
                }
            } finally {
                c.close();
            }
        }
        return size != 0 ? size : MAX_RESOLUTION;
    }
}
