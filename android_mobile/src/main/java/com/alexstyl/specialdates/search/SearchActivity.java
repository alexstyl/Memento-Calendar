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
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.AndroidDateLabelCreator;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.permissions.PermissionNavigator;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.transition.FadeInTransition;
import com.alexstyl.specialdates.transition.FadeOutTransition;
import com.alexstyl.specialdates.transition.SimpleTransitionListener;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;
import com.novoda.notils.text.SimpleTextWatcher;

import javax.inject.Inject;

import static android.view.View.GONE;
import static com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;

/**
 * A fragment in which the user can search for namedays and their contact's birthdays.
 * <br/>The fragment has a different logic for when the user has enabled namedays for any language.
 * If the user has enabled to display Namedays, the search EditText will give no suggestions. Instead a custom
 * suggestion bar on top of the keyboard is going to be given to the user with names.
 * <p>Created by alexstyl on 20/04/15.</p>
 */
public class SearchActivity extends ThemedMementoActivity {

    private static final String KEY_QUERY = "alexstyl:key_query";
    private static final int ID_CONTACTS = 31;
    private static final int ID_NAMEDAYS = 32;
    private static final int INITAL_COUNT = 5;
    private static final int HALF = 2;
    private static final int COLUMNS = 3;

    private int searchCounter = INITAL_COUNT;
    private SearchBar searchbar;
    private RecyclerView namesSuggestionsView;
    private SearchResultAdapter adapter;
    private NameSuggestionsAdapter namesAdapter;
    private String searchQuery;

    private ViewFader fader = new ViewFader();
    private ViewGroup content;
    private ContactPermissionRequest permissions;
    private RecyclerView resultView;
    private PeopleEventsSearch peopleEventsSearch;
    private ContactEventViewModelFactory viewModelFactory;
    private SearchNavigator searchNavigator;
    @Inject Analytics analytics;
    @Inject StringResources stringResources;
    @Inject ImageLoader imageLoader;
    @Inject NamedayUserSettings namedayUserSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        peopleEventsSearch = new PeopleEventsSearch(PeopleEventsProvider.newInstance(context(), namedayUserSettings), NameMatcher.INSTANCE);
        DateLabelCreator dateLabelCreator = new AndroidDateLabelCreator(this);
        viewModelFactory = new ContactEventViewModelFactory(new ContactEventLabelCreator(Date.Companion.today(), stringResources, dateLabelCreator));

        analytics.trackScreen(Screen.SEARCH);
        searchNavigator = new SearchNavigator(this, analytics);

        searchbar = Views.findById(this, R.id.search_searchbar);
        setSupportActionBar(searchbar);
        content = Views.findById(this, R.id.search_content);

        resultView = Views.findById(this, android.R.id.list);
        resultView.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.search_result_card_vertical_padding) / HALF;
        resultView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, COLUMNS));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context());
        resultView.setLayoutManager(mLayoutManager);

        namesSuggestionsView = Views.findById(this, R.id.nameday_suggestions);
        PermissionNavigator navigator = new PermissionNavigator(this, analytics);
        PermissionChecker checker = new PermissionChecker(this);
        permissions = new ContactPermissionRequest(navigator, checker, permissionCallbacks);

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_QUERY);
        }

        setupSearchField();

        adapter = new SearchResultAdapter(imageLoader);
        adapter.setSearchResultClickListener(listener);
        resultView.setAdapter(adapter);

        searchbar.setOnBackKeyPressedListener(onBackKeyPressedListener);

        setupSearchbarHint(namedayUserSettings);

        if (namedayUserSettings.isEnabled()) {
            GridLayoutManager namedayManager = new GridLayoutManager(context(), 1, RecyclerView.HORIZONTAL, false);
            namesAdapter = NameSuggestionsAdapter.newInstance(context(), onNameSelectedListener, namedayUserSettings);
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
            permissions.requestForPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissions.onActivityResult(requestCode, resultCode, data);
    }

    private void setupSearchbarHint(NamedayUserSettings preferences) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (namedayUserSettings.isEnabled()) {
            namesAdapter.clearNames();
        }
    }

    private void resetSearchCounter() {
        searchCounter = INITAL_COUNT;
    }

    private void updateNameSuggestions(String text) {
        if (namedayUserSettings.isEnabled()) {
            namesAdapter.getFilter().filter(text);
        }
    }

    private void setupSearchField() {
        searchbar.addTextWatcher(DelayedTextWatcher.newInstance(textUpdatedTextUpdatedCallback));
        if (namedayUserSettings.isEnabled()) {
            searchbar.addTextWatcher(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    namesAdapter.setTextTyped(s.toString());
                }
            });
        }
    }

    private final NameSuggestionsAdapter.OnNameSelectedListener onNameSelectedListener = new NameSuggestionsAdapter.OnNameSelectedListener() {
        @Override
        public void onNameSelected(String name) {
            AndroidUtils.requestHideKeyboard(context(), searchbar);
            onNameSet(name);
            resultView.requestFocus();
        }
    };

    private void onNameSet(String name) {
        // setting the text to the EditText will trigger the search for the name
        searchbar.setText(name);
        if (namedayUserSettings.isEnabled()) {
            namesAdapter.clearNames();
        }
    }

    private final SearchResultAdapter.SearchResultClickListener listener = new SearchResultAdapter.SearchResultClickListener() {

        @Override
        public void onContactClicked(Contact contact) {
            searchNavigator.toContactDetails(contact);
        }

        @Override
        public void onNamedayClicked(Date date) {
            Date currentYearDate = Date.Companion.on(date.getDayOfMonth(), date.getMonth(), Date.Companion.getCURRENT_YEAR());
            Intent intent = UpcomingEventsActivity.getStartIntent(context(), currentYearDate);
            startActivity(intent);
        }

    };

    private final LoaderManager.LoaderCallbacks<NameCelebrations> namedayLoaderCallbacks = new LoaderManager.LoaderCallbacks<NameCelebrations>() {

        @Override
        public Loader<NameCelebrations> onCreateLoader(int id, Bundle args) {
            return NamedaysLoader.newInstance(context(), searchQuery, namedayUserSettings);
        }

        @Override
        public void onLoadFinished(Loader<NameCelebrations> loader, NameCelebrations results) {
            adapter.setNamedays(results);
        }

        @Override
        public void onLoaderReset(Loader<NameCelebrations> loader) {
            adapter.setNamedays(new NameCelebrations(""));
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
            PeopleEventsObserver observer = new PeopleEventsObserver(getContentResolver());
            return new SearchLoader(context(), peopleEventsSearch, observer, searchQuery, searchCounter, viewModelFactory);
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
        }

        @Override
        public void onPermissionDenied() {
            namesSuggestionsView.post(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            });
        }
    };

}
