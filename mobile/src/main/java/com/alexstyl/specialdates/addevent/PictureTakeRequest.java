package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.FilePathProvider;
import com.alexstyl.specialdates.MementoApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

final class PictureTakeRequest {

    private static final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private final PackageManager packageManager;
    private final FilePathProvider filePathProvider;
    private final Context context;

    private Uri currentPhotoPath;

    PictureTakeRequest(PackageManager packageManager, FilePathProvider filePathProvider, Context context) {
        this.packageManager = packageManager;
        this.filePathProvider = filePathProvider;
        this.context = context;
    }

    void takeNewPicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ErrorTracker.track(new IOException("Error while starting picture", ex));
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(
                activity,
                MementoApplication.PACKAGE + ".fileprovider",
                photoFile
        );
        currentPhotoPath = photoUri;
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        List<ResolveInfo> resolvedIntentActivities = packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        activity.startActivityForResult(takePictureIntent, requestCode);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = filePathProvider.getExternalFilesDir();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    boolean canTakePicture() {
        return takePictureIntent.resolveActivity(packageManager) != null;
    }

    Uri getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    void finishRequest() {
        context.revokeUriPermission(
                currentPhotoPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION
        );
    }
}
