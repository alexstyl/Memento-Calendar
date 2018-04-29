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

public final class AvatarPickerView extends RelativeLayout implements ImageAware {

    private ImageView imageView;
    private ImageView gradientTopView;
    private ImageView gradientBottomView;
    private View icon;

    public AvatarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.merge_avatar_picker_view, this);

        icon = findViewById(R.id.avatar_picker_icon);
        imageView = Views.findById(this, R.id.avatar_picker_image);
        gradientTopView = Views.findById(this, R.id.avatar_picker_gradient__top);
        gradientBottomView = Views.findById(this, R.id.avatar_picker_gradient__bottom);
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
        if (drawable == null) {
            hideGradient();
        } else {
            showGradient();
        }
        imageView.setImageDrawable(drawable);
        return true;
    }

    private void hideGradient() {
        gradientTopView.setVisibility(GONE);
        gradientBottomView.setVisibility(GONE);
    }

    @Override
    public boolean setImageBitmap(Bitmap imageBitmap) {
        if (imageBitmap == null) {
            setImageDrawable(null);
            hideGradient();
        } else {
            imageView.setImageBitmap(imageBitmap);
            showGradient();
        }
        return true;
    }

    private void showGradient() {
        gradientTopView.setVisibility(VISIBLE);
        gradientBottomView.setVisibility(VISIBLE);
    }

    public boolean isDisplayingAvatar() {
        return imageView.getDrawable() != null;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            icon.setVisibility(VISIBLE);
        } else {
            icon.setVisibility(GONE);
        }
        super.setEnabled(enabled);
    }
}
