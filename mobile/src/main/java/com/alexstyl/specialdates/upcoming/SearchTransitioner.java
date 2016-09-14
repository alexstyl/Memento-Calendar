package com.alexstyl.specialdates.upcoming;

import android.app.Activity;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.Navigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.transition.FadeInTransition;
import com.alexstyl.specialdates.transition.FadeOutTransition;
import com.alexstyl.specialdates.transition.SimpleTransitionListener;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.util.Utils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class SearchTransitioner {

    private final Activity activity;
    private final Navigator navigator;
    private final ViewGroup activityContent;
    private final ExposedSearchToolbar toolbar;
    private final ViewFader viewFader;

    private final int toolbarMargin;

    public SearchTransitioner(Activity activity,
                              Navigator navigator,
                              ViewGroup activityContent,
                              ExposedSearchToolbar toolbar,
                              ViewFader viewFader) {
        this.activity = activity;
        this.navigator = navigator;
        this.activityContent = activityContent;
        this.toolbar = toolbar;
        this.viewFader = viewFader;

        this.toolbarMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_tight);
    }

    public void transitionToSearch() {
        if (supportsTransitions()) {

            Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
            TransitionManager.beginDelayedTransition(toolbar, transition);
            FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
            frameLP.setMargins(0, 0, 0, 0);
            toolbar.setLayoutParams(frameLP);
            viewFader.hideContentOf(toolbar);

            TransitionManager.beginDelayedTransition(activityContent, new Fade(Fade.OUT));
            activityContent.setVisibility(GONE);
        } else {
            navigator.toSearch();
        }
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                navigator.toSearch();
                activity.overridePendingTransition(0, 0);
            }
        };
    }

    private static boolean supportsTransitions() {
        return Utils.hasKitKat();
    }

    public void onActivityResumed() {
        if (supportsTransitions()) {
            TransitionManager.beginDelayedTransition(toolbar, FadeInTransition.createTransition());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin);
            viewFader.showContent(toolbar);
            toolbar.setLayoutParams(layoutParams);

            TransitionManager.beginDelayedTransition(activityContent, new Fade(Fade.IN));
            activityContent.setVisibility(VISIBLE);
        }
    }
}
