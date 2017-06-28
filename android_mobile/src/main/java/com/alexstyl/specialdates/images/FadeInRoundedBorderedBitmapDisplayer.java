package com.alexstyl.specialdates.images;

import android.content.res.Resources;
import android.graphics.Color;

import com.alexstyl.specialdates.R;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

class FadeInRoundedBorderedBitmapDisplayer extends CircleBitmapDisplayer {

    public static BitmapDisplayer newInstance(Resources resources) {
        int strokeWidth = resources.getDimensionPixelSize(R.dimen.facebook_avatar_stroke);
        return new CircleBitmapDisplayer(Color.WHITE, strokeWidth);
    }
}
