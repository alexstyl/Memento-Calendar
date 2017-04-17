package com.alexstyl.specialdates.ui.activity;

import android.app.Activity;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.transition.FadeInTransition;
import com.alexstyl.specialdates.transition.FadeOutTransition;
import com.alexstyl.specialdates.transition.SimpleTransitionListener;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.upcoming.view.ExposedSearchToolbar;
import com.alexstyl.android.Version;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

final class SearchTransitioner {

    private final Activity activity;
    private final MainNavigator navigator;
    private final ViewGroup activityContent;
    private final ExposedSearchToolbar toolbar;
    private final ViewFader viewFader;

    private final int toolbarMargin;
    private boolean transitioning;

    SearchTransitioner(Activity activity,
                       MainNavigator navigator,
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

    void transitionToSearch() {
        if (transitioning) {
            return;
        }
        if (supportsTransitions()) {

            Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
            TransitionManager.beginDelayedTransition(toolbar, transition);
            expandToolbar();
            viewFader.hideContentOf(toolbar);

            TransitionManager.beginDelayedTransition(activityContent, new Fade(Fade.OUT));
            activityContent.setVisibility(GONE);
        } else {
            navigator.toSearch();
        }
    }

    private void expandToolbar() {
        FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        frameLP.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(frameLP);
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {

            @Override
            public void onTransitionStart(Transition transition) {
                transitioning = true;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transitioning = false;
                navigator.toSearch();
                activity.overridePendingTransition(0, 0);
            }
        };
    }

    private static boolean supportsTransitions() {
        return Version.hasKitKat();
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
