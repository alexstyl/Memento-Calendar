package com.alexstyl.specialdates.upcoming.widget.today;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

public class UpcomingWidgetPreviewLayout extends FrameLayout {

    private final View background;
    private final ImageView avatar;
    private final TextView header;
    private final TextView contactNames;
    private static final TransparencyColorCalculator transparencyColorCalculator = new TransparencyColorCalculator();

    private WidgetVariant selectedVariant = WidgetVariant.LIGHT;
    private float opacityLevel = 1.0f;

    public UpcomingWidgetPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_widget_preview, this, true);

        this.header = Views.findById(this, R.id.upcoming_widget_header);
        this.contactNames = Views.findById(this, R.id.upcoming_widget_events_text);
        this.avatar = Views.findById(this, R.id.widget_avatar);
        this.background = Views.findById(this, R.id.upcoming_widget_background_image);
        this.avatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact_picture));

        if (isInEditMode()) {
            header.setText("This is a Header");
            contactNames.setText("This is names");
        }
    }

    public void previewWidgetVariant(WidgetVariant variant) {
        selectedVariant = variant;
        setTextColor(variant.getTextColor());
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        int color = getResources().getColor(selectedVariant.getBackgroundColorResId());
        int newBackgroundColor = transparencyColorCalculator.calculateColor(color, opacityLevel);
        background.setBackgroundColor(newBackgroundColor);
    }

    public void previewBackgroundOpacityLevel(float opacityLevel) {
        this.opacityLevel = opacityLevel;
        updateBackgroundColor();
    }

    private void setTextColor(@ColorRes int textColorRes) {
        int textColor = getResources().getColor(textColorRes);
        header.setTextColor(textColor);
        contactNames.setTextColor(textColor);
    }

    public void setTitle(String title) {
        header.setText(title);
    }

    public void setSubtitle(@StringRes int stringResId) {
        String subtitle = getResources().getString(stringResId);
        contactNames.setText(subtitle);
    }
}
