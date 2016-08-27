package com.alexstyl.specialdates.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;

public class MementoCardView extends FrameLayout {

    public MementoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MementoCardView, 0, 0);
        int cardColor = typedArray.getColor(R.styleable.MementoCardView_cardBackgroundColor, 0);
        typedArray.recycle();

        super.setBackgroundResource(R.drawable.card_no_paddings);
        if (isAValidColor(cardColor)) {
            getBackground().setColorFilter(cardColor, PorterDuff.Mode.MULTIPLY);
        }
    }

    private boolean isAValidColor(int backgroundColor) {
        return backgroundColor != 0;
    }

}
