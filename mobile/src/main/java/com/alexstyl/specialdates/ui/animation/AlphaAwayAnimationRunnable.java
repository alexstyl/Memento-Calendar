package com.alexstyl.specialdates.ui.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

/**
 * <p>Created by Alex on 9/18/2014.</p>
 */
final public class AlphaAwayAnimationRunnable implements Runnable {

    private Context mContext;
    private View mView;

    public static final long ANIMATON_DURATION = 750l;

    public AlphaAwayAnimationRunnable(Context context, View view, boolean comingIn) {
        this.mContext = context;
        this.mView = view;
        this.mView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void run() {
        Animation anim = AlphaAwayAnimation.makeAnimation(mContext, true);
        anim.setDuration(ANIMATON_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mAnimationEndListener != null) {
                    mAnimationEndListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mView.startAnimation(anim);
    }

    public interface AnimationEndListener {
        public void onAnimationEnd(Animation animation);
    }

    private AnimationEndListener mAnimationEndListener;

    public void setOnAnimationEndListener(AnimationEndListener l) {
        this.mAnimationEndListener = l;

    }
}
