package com.alexstyl.specialdates.addevent;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

final public class ImageIntentFactory {

    static String ACTION_IMAGE_PICK = Intent.ACTION_PICK;
    static String ACTION_IMAGE_CAPTURE = MediaStore.ACTION_IMAGE_CAPTURE;

    public Intent pickExistingImage() {
        Intent intent = new Intent(ACTION_IMAGE_PICK);
        intent.setType("image/*");
        intent.addFlags(
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION
        );
        return intent;
    }

    public Intent captureNewPhoto(Uri outputUri) {
        Intent takePictureIntent = new Intent(ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        takePictureIntent.addFlags(
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return takePictureIntent;
    }
}
