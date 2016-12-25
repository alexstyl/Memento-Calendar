package com.alexstyl.specialdates;

import android.content.Context;
import android.os.Environment;

import java.io.File;

final public class FilePathProvider {

    private final Context context;

    public FilePathProvider(Context context) {
        this.context = context;
    }

    public File getExternalFilesDir() {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }
}
