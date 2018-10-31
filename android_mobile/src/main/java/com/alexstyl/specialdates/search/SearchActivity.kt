package com.alexstyl.specialdates.search

import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration
import javax.inject.Inject
import com.alexstyl.specialdates.ui.ViewFader
import com.alexstyl.specialdates.ui.widget.HorizontalSpacesItemDecoration
import com.alexstyl.specialdates.transition.FadeInTransition
import android.view.ViewTreeObserver


class SearchActivity : ThemedMementoActivity() {

    @Inject lateinit var presenter: SearchPresenter
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var labelCreator: DateLabelCreator
    @Inject lateinit var analytics: Analytics
    @Inject lateinit var namedayUserSettings: NamedayUserSettings
    @Inject lateinit var namedayCalendarProvider: NamedayCalendarProvider

    private lateinit var searchResultView: SearchResultView
    private val navigator by lazy {
        SearchNavigator(this, analytics)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        val searchbar = findViewById<Searchbar>(R.id.search_searchbar)
        val resultView = findViewById<RecyclerView>(R.id.search_results).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.search_result_vertical_padding)))
        }
        val namesSuggestionsView = findViewById<RecyclerView>(R.id.search_nameday_suggestions).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        searchbar.onSoftBackKeyPressed = {
            if (searchbar.text.isEmpty()) {
                finish()
                true
            } else {
                false
            }
        }
        searchbar.setOnNavigateBackButtonPressed {
            finish()
        }
        searchbar.setOnClearButtonPressed {
            searchbar.clearText()
        }
        val resultsAdapter = SearchResultsAdapter(imageLoader, object : SearchResultClickListener {
            override fun onContactClicked(contact: Contact) {
                navigator.toContactDetails(contact)
            }

            override fun onNamedayClicked(date: Date) {
                navigator.toNamedays(date)
            }

            override fun onContactReadPermissionClicked() {
                navigator.toContactPermission()
            }
        })
        resultView.adapter = resultsAdapter
        if (savedInstanceState == null) {
            introduceSearchbar(searchbar)
        }
        if (namedayUserSettings.isEnabled) {
            val namesAdapter = NameSuggestionsAdapter { name ->
                searchbar.setText(name)
            }
            namesSuggestionsView.adapter = namesAdapter
            namesSuggestionsView.addItemDecoration(HorizontalSpacesItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.search_nameday_results_vertical_padding))
            )
            searchResultView = AndroidSearchResultView(resultsAdapter, searchbar, namesAdapter)
        } else {
            namesSuggestionsView.visibility = View.GONE
            searchResultView = AndroidSearchResultView(resultsAdapter, searchbar, null)
        }

    }

    private fun introduceSearchbar(searchbar: Searchbar) {
        ViewFader.hideContentOf(searchbar)
        val viewTreeObserver = searchbar.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                searchbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                animateShowSearch()
            }

            private fun animateShowSearch() {
                TransitionManager.beginDelayedTransition(searchbar, FadeInTransition.createTransition())
                ViewFader.showContent(searchbar)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.presentInto(searchResultView)
    }

    override fun onStop() {
        super.onStop()
        presenter.stopPresenting()
    }
}
