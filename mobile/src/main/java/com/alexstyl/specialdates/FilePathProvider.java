package com.alexstyl.specialdates;

import android.content.Context;
import android.net.Uri;

import java.io.File;

final public class FilePathProvider {

    private final Context context;

    public FilePathProvider(Context context) {
        this.context = context;
    }

    public Uri getExternalFilesDir() {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }
}
