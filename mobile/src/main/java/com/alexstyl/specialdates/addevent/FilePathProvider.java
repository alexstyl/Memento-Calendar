package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public final class FilePathProvider {

    private final Context context;

    public FilePathProvider(Context context) {
        this.context = context;
    }

    public Uri createTemporaryCacheFile() {
//        File getImage = context.getExternalCacheDir();
//        return Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        return getCaptureImageOutputUri(context);
    }

    private static Uri getCaptureImageOutputUri(@NonNull Context context) {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }
    //file:///storage/emulated/0/Android/data/com.example.croppersample/cache/pickImageResult.jpeg

}
