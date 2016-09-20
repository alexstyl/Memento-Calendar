package com.alexstyl.specialdates.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.alexstyl.specialdates.Navigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.transition.FadeInTransition;
import com.alexstyl.specialdates.transition.FadeOutTransition;
import com.alexstyl.specialdates.transition.SimpleTransitionListener;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;
import com.novoda.notils.meta.AndroidUtils;

import static android.view.View.GONE;
import static com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;

/**
 * A fragment in which the user can search for namedays and their contact's birthdays.
 * <br/>The fragment has a different logic for when the user has enabled namedays for any language.
 * If the user has enabled to display Namedays, the search EditText will give no suggestions. Instead a custom
 * suggestion bar on top of the keyboard is going to be given to the user with names.
 * <p>Created by alexstyl on 20/04/15.</p>
 */
public class SearchActivity extends ThemedActivity {

    private static final String KEY_QUERY = "alexstyl:key_query";
    private static final int ID_CONTACTS = 31;
    private static final int ID_NAMEDAYS = 32;
    private static final int INITAL_COUNT = 5;

    private int searchCounter = INITAL_COUNT;
    private SearchBar searchbar;
    private RecyclerView resultView;
    private RecyclerView namesSuggestionsView;
    private SearchResultAdapter adapter;
    private NameSuggestionsAdapter namesAdapter;
    private String searchQuery;

    private ViewFader fader = new ViewFader();
    private ViewGroup content;
    private NamedayPreferences namedayPreferences;
    private ContactPermissionRequest permissions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Analytics analytics = AnalyticsProvider.getAnalytics(this);
        analytics.trackScreen(Screen.SEARCH);
        searchbar = Views.findById(this, R.id.search_searchbar);
        setSupportActionBar(searchbar);
        content = Views.findById(this, R.id.search_content);
        resultView = Views.findById(this, android.R.id.list);
        resultView.setHasFixedSize(false);
        namesSuggestionsView = Views.findById(this, R.id.nameday_suggestions);
        PermissionChecker checker = new PermissionChecker(this);
        Navigator navigator = new Navigator(this, analytics);
        permissions = new ContactPermissionRequest(navigator, checker, permissionCallbacks);

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_QUERY);
        }

        setupSearchField();

        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(getResources());
        int year = DayDate.today().getYear();
        NamedayLocale locale = NamedayPreferences.newInstance(this).getSelectedLanguage();
        NamedayCalendar namedayCalendar = NamedayCalendarProvider.newInstance(this.getResources()).loadNamedayCalendarForLocale(locale, year);

        adapter = new SearchResultAdapter(imageLoader, namedayCalendar);
        adapter.setSearchResultClickListener(listener);

        resultView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_between);
        resultView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 3));
        resultView.setLayoutManager(mLayoutManager);
        resultView.setAdapter(adapter);

        searchbar.setOnBackKeyPressedListener(onBackKeyPressedListener);

        namedayPreferences = NamedayPreferences.newInstance(this);
        setupSearchbarHint(namedayPreferences);

        if (namedayPreferences.isEnabled()) {
            // we are loading namedays as well
            GridLayoutManager namedayManager = new GridLayoutManager(context(), 1, RecyclerView.HORIZONTAL, false);
            namesAdapter = NameSuggestionsAdapter.newInstance(context());
            namesAdapter.setOnNameSelectedListener(onNameSelectedListener);
            namesSuggestionsView.setHasFixedSize(true);
            namesSuggestionsView.setLayoutManager(namedayManager);
            namesSuggestionsView.setAdapter(namesAdapter);

            searchbar.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            searchbar.setOnFocusChangeListener(new ToggleVisibilityOnFocus(namesSuggestionsView));
        } else {
            namesSuggestionsView.setVisibility(GONE);
        }

        if (savedInstanceState == null) {
            fader.hideContentOf(searchbar);
            ViewTreeObserver viewTreeObserver = searchbar.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    searchbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    animateShowSearch();
                }

                private void animateShowSearch() {
                    TransitionManager.beginDelayedTransition(searchbar, FadeInTransition.createTransition());
                    fader.showContent(searchbar);
                }
            });
        }

        if (!permissions.permissionIsPresent()) {
            Log.d("requesting permission");
            permissions.requestForPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissions.onActivityResult(requestCode, resultCode, data);
    }

    private void setupSearchbarHint(NamedayPreferences preferences) {
        SearchHintCreator searchHintCreator = new SearchHintCreator(getResources(), preferences);
        searchbar.setHint(searchHintCreator.createHint());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem clearMenuItem = menu.findItem(R.id.action_clear);
        clearMenuItem.setVisible(searchbar.hasText());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_clear:
                onClearPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        if (supportsTransitions()) {
            AndroidUtils.requestHideKeyboard(this, searchbar);
            exitTransitionWithAction(new Runnable() {
                @Override
                public void run() {
                    SearchActivity.super.finish();
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            super.finish();
        }
    }

    private void exitTransitionWithAction(final Runnable endingAction) {
        Transition transition = FadeOutTransition.withAction(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                endingAction.run();
            }
        });

        TransitionManager.beginDelayedTransition(searchbar, transition);
        fader.hideContentOf(searchbar);

        TransitionManager.beginDelayedTransition(content, new Fade(Fade.OUT));
    }

    private void onClearPressed() {
        searchbar.clearText();
        AndroidUtils.toggleKeyboard(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_QUERY, searchQuery);
    }

    private void startSearching() {
        getSupportLoaderManager().restartLoader(ID_CONTACTS, null, contactSearchCallbacks);
        getSupportLoaderManager().restartLoader(ID_NAMEDAYS, null, namedayLoaderCallbacks);
    }

    private void clearResults() {
        adapter.clearResults();
        if (namedayPreferences.isEnabled()) {
            namesAdapter.clearNames();
        }
    }

    private void resetSearchCounter() {
        searchCounter = INITAL_COUNT;
    }

    private void updateNameSuggestions(String text) {
        if (namedayPreferences.isEnabled()) {
            namesAdapter.getFilter().filter(text);
        }
    }

    private void setupSearchField() {
        searchbar.addTextWatcher(DelayedTextWatcher.newInstance(textUpdatedTextUpdatedCallback));
    }

    private void onNameSet(String name) {
        // setting the text to the EditText will trigger the search for the name
        AndroidUtils.requestHideKeyboard(this, searchbar);
        searchbar.setText(name);
        searchbar.clearFocus();
        if (namedayPreferences.isEnabled()) {
            namesAdapter.clearNames();
        }
    }

    private final NameSuggestionsAdapter.OnNameSelectedListener onNameSelectedListener = new NameSuggestionsAdapter.OnNameSelectedListener() {
        @Override
        public void onNameSelected(View view, String name) {
            onNameSet(name);
        }
    };

    private final SearchResultAdapter.SearchResultClickListener listener = new SearchResultAdapter.SearchResultClickListener() {

        @Override
        public void onContactClicked(View v, Contact contact) {
            contact.displayQuickInfo(context(), v);
        }

        @Override
        public void onNamedayClicked(View v, int month, int day) {
            DayDate dayDate = DayDate.today();
            DateDetailsActivity.startActivity(context(), month, day, dayDate.getYear());
        }

    };

    private final LoaderManager.LoaderCallbacks<NameCelebrations> namedayLoaderCallbacks = new LoaderManager.LoaderCallbacks<NameCelebrations>() {

        @Override
        public Loader<NameCelebrations> onCreateLoader(int id, Bundle args) {
            return NamedaysLoader.newInstance(context(), searchQuery);
        }

        @Override
        public void onLoadFinished(Loader<NameCelebrations> loader, NameCelebrations results) {
            adapter.setNamedays(results);
        }

        @Override
        public void onLoaderReset(Loader<NameCelebrations> loader) {
            adapter.setNamedays(NameCelebrations.EMPTY);
        }
    };

    private final DelayedTextWatcher.TextUpdatedCallback textUpdatedTextUpdatedCallback = new DelayedTextWatcher.TextUpdatedCallback() {
        @Override
        public void onTextChanged(String text) {
            searchQuery = text;
            updateNameSuggestions(text);
            resetSearchCounter();
            invalidateOptionsMenu();
        }

        @Override
        public void onEmptyTextConfirmed() {
            clearResults();
            invalidateOptionsMenu();
        }

        @Override
        public void onTextConfirmed(String text) {
            startSearching();
        }
    };

    private final LoaderManager.LoaderCallbacks<SearchResults> contactSearchCallbacks = new LoaderManager.LoaderCallbacks<SearchResults>() {

        @Override
        public Loader<SearchResults> onCreateLoader(int id, Bundle args) {
            adapter.notifyIsLoadingMore();
            return new SearchLoader(context(), searchQuery, searchCounter);
        }

        @Override
        public void onLoadFinished(Loader<SearchResults> loader, SearchResults searchResults) {
            if (loader.getId() == ID_CONTACTS) {
                adapter.updateSearchResults(searchResults);
            }
        }

        @Override
        public void onLoaderReset(Loader<SearchResults> loader) {
            if (loader.getId() == ID_CONTACTS) {
                adapter.notifyIsLoadingMore();
            }
        }
    };

    private final OnBackKeyPressedListener onBackKeyPressedListener = new OnBackKeyPressedListener() {
        @Override
        public boolean onBackButtonPressed() {
            if (searchbar.hasText()) {
                // do nothing
                return false;
            } else {
                finish();
                return true;
            }
        }
    };

    private final PermissionCallbacks permissionCallbacks = new PermissionCallbacks() {
        @Override
        public void onPermissionGranted() {
            // do nothing.
            Log.d("permission granted");
        }

        @Override
        public void onPermissionDenied() {
            Log.d("permission denied");
            namesSuggestionsView.post(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            });
        }
    };

}
