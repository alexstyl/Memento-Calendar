package com.alexstyl.specialdates.addevent;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

final class ImagePickerIntent implements ImagePickIntent {

    @Override
    public Intent createIntentWithOutput(Uri photoUri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));
        return intent;
    }
}
