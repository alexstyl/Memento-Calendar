package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

public class CompactCardView extends FrameLayout {

    public CompactCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.base_card_compact, this, false);
        addView(view);

        TextView title = (TextView) findViewById(R.id.search_result_contact_name);
        TextView subtitle = (TextView) findViewById(R.id.event_label);
        ColorImageView heroImage = (ColorImageView) findViewById(R.id.search_result_avatar);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CompactCardView,
                0, 0
        );

        int color;
        String primaryText;
        String secondaryText;
        Drawable drawable;
        try {
            color = typedArray.getColor(R.styleable.CompactCardView_backgroundHeroColor, LetterPainter.getVariant(getResources(), 0));
            primaryText = typedArray.getString(R.styleable.CompactCardView_primaryText);
            secondaryText = typedArray.getString(R.styleable.CompactCardView_secondaryText);
            drawable = typedArray.getDrawable(R.styleable.CompactCardView_android_src);
        } finally {
            typedArray.recycle();
        }
        heroImage.setBackgroundColor(color);
        title.setText(primaryText);
        subtitle.setText(secondaryText);
        heroImage.getImageView().setImageDrawable(drawable);

        invalidate();
        requestLayout();
    }
}
