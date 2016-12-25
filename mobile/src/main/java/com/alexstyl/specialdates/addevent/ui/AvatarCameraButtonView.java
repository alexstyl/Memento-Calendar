package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alexstyl.specialdates.R;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.novoda.notils.caster.Views;

final public class AvatarCameraButtonView extends RelativeLayout implements ImageAware {

    private ImageView imageView;

    public AvatarCameraButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.merge_camera_button_image_view, this);

        imageView = Views.findById(this, R.id.camera_button_image);
    }

    @Override
    public ViewScaleType getScaleType() {
        return ViewScaleType.fromImageView(imageView);
    }

    @Override
    public View getWrappedView() {
        return imageView;
    }

    @Override
    public boolean isCollected() {
        return false;
    }

    @Override
    public boolean setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        return true;
    }

    @Override
    public boolean setImageBitmap(Bitmap imageBitmap) {
        if (imageBitmap == null) {
            setImageDrawable(null);
        } else {
            imageView.setImageBitmap(imageBitmap);
        }
        return true;
    }

    public boolean isDisplayingAvatar() {
        return imageView.getDrawable() != null;
    }
}
