package com.alexstyl.specialdates.ui.animation;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.alexstyl.specialdates.R;

/**
 * Created by Alex on 6/20/2014.
 */
public class AlphaAwayAnimation extends AlphaAnimation {

    private AlphaAwayAnimation() {
        super(1, 0);
    }

    public static Animation makeAnimation(Context context, boolean comingIn) {

        int animRes;
        if (comingIn) {
            animRes = R.anim.slide_in_below;
        } else {
            animRes = R.anim.slide_out_above;
        }
        Animation anim = AnimationUtils.loadAnimation(context, animRes);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setStartTime(AnimationUtils.currentAnimationTimeMillis());
        return anim;
    }
}
