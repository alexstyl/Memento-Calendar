package com.alexstyl.specialdates.addevent;

import android.content.Context;

import com.alexstyl.specialdates.FilePathProvider;
import com.novoda.notils.logger.simple.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class TempImageFile {

    public static File newInstance(Context context) {
        FilePathProvider pathProvider = new FilePathProvider(context.getApplicationContext());
        try {
            return createImageFile(pathProvider);
        } catch (IOException ex) {
            Log.e(ex);
            return null;
        }
    }

    private static File createImageFile(FilePathProvider filePathProvider) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = filePathProvider.getExternalFilesDir();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
}
