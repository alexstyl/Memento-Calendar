package com.alexstyl.specialdates.images;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.util.Utils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageDownloader extends BaseImageDownloader {

    public ImageDownloader(Context context) {
        super(context);
    }

    @Override
    public InputStream getStream(String imageUri, Object extra) throws IOException {
        // Collage example
        return super.getStream(imageUri, extra);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse(imageUri);

        if (imageUri.startsWith("content://com.android.contacts/")) {
            if (Utils.hasICS()) {
                return ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true);
            } else {
                return ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
            }
        }

        return res.openInputStream(uri);

    }
}
