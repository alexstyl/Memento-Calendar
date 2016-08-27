package com.alexstyl.specialdates.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.alexstyl.specialdates.ui.base.MementoFragment;

import java.io.InputStream;


public class HDAvatarFragment extends MementoFragment {

    public interface HDLoading {
        /**
         * Called whenever The HDAvatarFragment has finished loading the HD
         * image of the contact requested
         *
         * @param image The image of the requested contact. Can be null if the
         *              contact has no set image
         */
        void onContactLoaded(BitmapDrawable image);
    }

    public static final boolean DEBUG = true;

    public HDAvatarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private BitmapDrawable mBitmap;

    public BitmapDrawable getBitmap() {
        return mBitmap;
    }

    @Override
    public void onDestroy() {
        recycle();
        super.onDestroy();

    }

    private long mContactID;
    private HDLoading mLoadingListener;
    private Context mContext;
    private LoadingTaskSerialOperation mTask;

    private void recycle() {
        if (mBitmap != null && mBitmap.getBitmap() != null) {
            mBitmap.getBitmap().recycle();
            mBitmap = null;
        }
    }

    public void loadContact(Context context, long contactID, HDLoading listener) {
        this.mContext = context.getApplicationContext();
        this.mLoadingListener = listener;

        if (contactID == mContactID && (mBitmap != null && mBitmap.getBitmap() != null)) {
            // we've already loaded this image. deliver it
            deliverBitmap();
        } else {
            this.mContactID = contactID;
            if (mTask != null && !mTask.isCancelled())
                mTask.cancel(true);
            mTask = new LoadingTaskSerialOperation();
            mTask.execute();
        }

    }

    private void deliverBitmap() {
        if (mLoadingListener != null)
            mLoadingListener.onContactLoaded(mBitmap);
    }

    private class LoadingTaskSerialOperation extends SerialOperationAsyncTask<Void, Void, BitmapDrawable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        protected BitmapDrawable doInBackground(Void... params) {

            InputStream inputStream = null;
            if (Utils.hasICS()) {
                inputStream = Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                        ContentUris.withAppendedId(Contacts.CONTENT_URI, mContactID), true);
            } else {
                inputStream = Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                        ContentUris.withAppendedId(Contacts.CONTENT_URI, mContactID));
            }

            Bitmap thumb = BitmapFactory.decodeStream(inputStream);
            if (thumb == null)
                return null;
            Bitmap blurred = fastblur(thumb, 12);
            thumb.recycle();
            if (isCancelled()) {
                blurred.recycle();
                return null;
            }
            return new BitmapDrawable(mContext.getResources(), blurred);
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            mBitmap = result;
            deliverBitmap();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap fastblur(Bitmap sentBitmap, int radius) {


        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(mContext);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius /* e.g. 3.f */);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;

    }

}
