package com.alexstyl.specialdates.addevent;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.util.Utils;

final class ImagePickerIntent implements ImagePickIntent {

    @Override
    public Intent createIntentWithOutput(Uri photoUri) {
        Intent intent = createPickAnImageIntent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    @NonNull
    private Intent createPickAnImageIntent() {
        Intent intent = new Intent();

        if (Utils.hasKitKat()) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
        }
        intent.setType("image/*");
        return intent;
    }
}
