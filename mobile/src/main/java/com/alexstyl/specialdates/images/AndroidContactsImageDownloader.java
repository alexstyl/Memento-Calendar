package com.alexstyl.specialdates.images;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AndroidContactsImageDownloader extends BaseImageDownloader {

    public AndroidContactsImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse(imageUri);

        if (imageUri.startsWith("content://com.android.contacts/")) {
            return ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true);
        }

        return res.openInputStream(uri);

    }
}
