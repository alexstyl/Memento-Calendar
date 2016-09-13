package com.alexstyl.specialdates.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.about.AboutActivity;
import com.alexstyl.specialdates.addevent.AddBirthdayActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.support.SupportDonateDialog;
import com.alexstyl.specialdates.transition.FadeInTransition;
import com.alexstyl.specialdates.transition.FadeOutTransition;
import com.alexstyl.specialdates.transition.SimpleTransitionListener;
import com.alexstyl.specialdates.ui.ThemeReapplier;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.upcoming.ExposedSearchToolbar;
import com.alexstyl.specialdates.util.Notifier;
import com.alexstyl.specialdates.util.Utils;
import com.alexstyl.specialdates.widgetprovider.TodayWidgetProvider;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

import static android.view.View.*;

/*
 * The activity was first launched with MainActivity being in package.ui.activity
  * For that reason, it needs to stay here so that we don't remove ourselves from the user's desktop
 */
public class MainActivity extends ThemedActivity {

    private Notifier notifier;
    private AskForSupport askForSupport;
    private ThemeReapplier reapplier;
    private Analytics analytics;
    private ExposedSearchToolbar toolbar;
    private FloatingActionButton addBirthdayFAB;
    private ViewGroup content;

    private int toolbarMargin;
    private ViewFader viewFader = new ViewFader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reapplier = new ThemeReapplier(this);

        analytics = Analytics.get(this);
        analytics.trackScreen(Screen.HOME);

        toolbar = Views.findById(this, R.id.memento_toolbar);
        toolbar.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);

        content = Views.findById(this, R.id.main_content);
        toolbarMargin = getResources().getDimensionPixelSize(R.dimen.padding_tight);

        notifier = Notifier.newInstance(this);

        addBirthdayFAB = Views.findById(this, R.id.main_birthday_add_fab);
        addBirthdayFAB.setOnClickListener(startAddBirthdayOnClick);
        askForSupport = new AskForSupport(this);

        setTitle(R.string.search_hint);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reapplier.hasThemeChanged()) {
            reapplyTheme();
        } else if (askForSupport.shouldAskForRating()) {
            askForSupport.askForRatingFromUser(this);
        }
        fadeContentIn();
    }

    private void fadeContentIn() {
        if (Utils.hasLollipop()) {
            addBirthdayFAB.show();
            TransitionManager.beginDelayedTransition(toolbar, FadeInTransition.createTransition());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin);
            viewFader.showContent(toolbar);
            toolbar.setLayoutParams(layoutParams);

            TransitionManager.beginDelayedTransition(content, new Fade(Fade.IN));
            content.setVisibility(VISIBLE);
        }
    }

    private void startAddBirthdayActivity() {
        Intent intent = new Intent(this, AddBirthdayActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);

        boolean hasBilling = SupportDonateDialog.isBillingAvailable(this);
        menu.findItem(R.id.action_donate).setVisible(hasBilling);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                break;
            case R.id.action_about:
                openAboutScreen();
                break;
            case R.id.action_donate:
                openDonateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDonateDialog() {
        analytics.trackScreen(Screen.DONATE);
        SupportDonateDialog.displayDialog(this);
    }

    private void openAboutScreen() {
        startActivity(new Intent(this, AboutActivity.class));

    }

    private void openSettings() {
        startActivity(new Intent(this, MainPreferenceActivity.class));
    }

    @Override
    public boolean onSearchRequested() {
        navigateToSearch();
        return true;
    }

    private final OnClickListener onToolbarClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AndroidUtils.toggleKeyboard(v.getContext());
            transitionToSearch();
        }

    };

    private void transitionToSearch() {
        if (Utils.hasLollipop()) {
            addBirthdayFAB.hide();

            Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
            TransitionManager.beginDelayedTransition(toolbar, transition);
            FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
            frameLP.setMargins(0, 0, 0, 0);
            toolbar.setLayoutParams(frameLP);
            viewFader.hideContentOf(toolbar);

            TransitionManager.beginDelayedTransition(content, new Fade(Fade.OUT));
            content.setVisibility(GONE);
        } else {
            navigateToSearch();
        }
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        };
    }

    private void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        notifier.cancelAllEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            TodayWidgetProvider.updateWidgets(this);
        }
    }

    private final OnClickListener startAddBirthdayOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddBirthdayActivity();
        }
    };
}
