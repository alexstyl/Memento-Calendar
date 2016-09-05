package com.alexstyl.specialdates.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

/**
 * A fragment in which the user can search for namedays and their contact's birthdays.
 * <br/>The fragment has a different logic for when the user has enabled namedays for any language.
 * If the user has enabled to display Namedays, the search EditText will give no suggestions. Instead a custom
 * suggestion bar on top of the keyboard is going to be given to the user with names.
 * <p>Created by alexstyl on 20/04/15.</p>
 */
public class SearchActivity extends MementoActivity {

    private static final String KEY_QUERY = "alexstyl:key_query";
    private static final int ID_CONTACTS = 31;
    private static final int ID_NAMEDAYS = 32;
    private static final int INITAL_COUNT = 5;

    private int searchCounter = INITAL_COUNT;
    private EditText searchField;
    private ImageButton clearButton;
    private RecyclerView resultView;
    private RecyclerView namesSuggestionsView;
    private SearchResultAdapter adapter;
    private NameSuggestionsAdapter namesAdapter;
    private boolean displayNamedays;
    private String searchQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Themer.get().initialiseActivity(this);
        Analytics.get(this).trackScreen(Screen.SEARCH);

        displayNamedays = shouldIncludeNamedays();
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_QUERY);
        }
        searchField = Views.findById(this, R.id.text_search_query);
        setupSearchField();
        ImageButton closeButton = Views.findById(this, R.id.btn_close);
        clearButton = Views.findById(this, R.id.btn_clear);
        resultView = Views.findById(this, android.R.id.list);
        resultView.setHasFixedSize(false);
        namesSuggestionsView = Views.findById(this, R.id.nameday_suggestions);

        closeButton.setOnClickListener(onCloseButtonClickListener);
        clearButton.setOnClickListener(onClearButtonClickedListener);

        adapter = SearchResultAdapter.newInstance(context(), displayNamedays);
        adapter.setSearchResultClickListener(listener);

        resultView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_between);
        resultView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 3));
        resultView.setLayoutManager(mLayoutManager);
        resultView.setAdapter(adapter);

        if (displayNamedays) {
            // we are loading namedays as well
            GridLayoutManager namedayManager = new GridLayoutManager(context(), 1, RecyclerView.HORIZONTAL, false);
            namesAdapter = NameSuggestionsAdapter.newInstance(context());
            namesAdapter.setOnNameSelectedListener(onNameSelectedListener);
            namesSuggestionsView.setHasFixedSize(true);
            namesSuggestionsView.setLayoutManager(namedayManager);
            namesSuggestionsView.setAdapter(namesAdapter);

            searchField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            searchField.setOnFocusChangeListener(onFocusChangeListener);
        } else {
            namesSuggestionsView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(searchQuery)) {
            hideClearButton();
        } else {
            showClearButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        searchField.requestFocus();
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
        if (displayNamedays) {
            namesAdapter.clearNames();
        }
    }

    private void hideClearButton() {
        clearButton.setVisibility(View.GONE);
    }

    private void showClearButton() {
        clearButton.setVisibility(View.VISIBLE);
    }

    private void resetSearchCounter() {
        searchCounter = INITAL_COUNT;
    }

    private boolean shouldIncludeNamedays() {
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context());
        return namedayPreferences.isEnabled();
    }

    private void updateNameSuggestions(String text) {
        if (displayNamedays) {
            namesAdapter.getFilter().filter(text);
        }
    }

    private void setupSearchField() {
        searchField.addTextChangedListener(DelayedTextWatcher.newInstance(textUpdatedTextUpdatedCallback));
        searchField.setOnEditorActionListener(onSearchFieldActionListener);
    }

    private void onNameSet(String name) {
        // setting the text to the EditText will trigger the search for the name
        searchField.setText(name);
        namesAdapter.clearNames();

        InputMethodManager imm = (InputMethodManager) context()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        searchField.clearFocus();

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

    private final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                namesSuggestionsView.setVisibility(View.VISIBLE);
            } else {
                namesSuggestionsView.setVisibility(View.GONE);
            }
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
        }

        @Override
        public void onEmptyTextEntered() {
            clearResults();
            hideClearButton();
        }

        @Override
        public void onTextConfirmed(String text) {
            startSearching();
            showClearButton();
        }
    };

    private final View.OnClickListener onClearButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchField.setText(null);
            searchField.requestFocus();
            InputMethodManager imm = (InputMethodManager) context()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);

        }
    };

    private final View.OnClickListener onCloseButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AndroidUtils.requestHideKeyboard(context(), v);
            ActivityCompat.finishAfterTransition(SearchActivity.this);
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

    private final TextView.OnEditorActionListener onSearchFieldActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) context()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                resultView.requestFocus();
                return true;
            }
            return false;
        }
    };

}
