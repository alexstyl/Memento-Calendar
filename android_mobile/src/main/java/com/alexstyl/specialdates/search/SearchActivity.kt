package com.alexstyl.specialdates.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import javax.inject.Inject

class SearchActivity : ThemedMementoActivity() {

    @Inject lateinit var presenter: SearchPresenter
    private lateinit var searchResultView: SearchResultView

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var labelCreator: DateLabelCreator
    @Inject lateinit var analytics: Analytics

    val navigator by lazy {
        SearchNavigator(this, analytics)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        val searchbar = findViewById<SearchToolbar>(R.id.search_searchbar)

//        val content = findViewById<ViewGroup>(R.id.search_content)
        val resultView = findViewById<RecyclerView>(R.id.search_results).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        val resultsAdapter = SearchResultsAdapter(imageLoader, labelCreator, object : SearchResultClickListener {
            override fun onContactClicked(contact: Contact) {
                navigator.toContactDetails(contact)
            }

            override fun onNamedayClicked(date: Date) {
                navigator.toNamedays(date)
            }
        })
        resultView.adapter = resultsAdapter
        searchResultView = AndroidSearchResultView(resultsAdapter, searchbar)

        val namesSuggestionsView = findViewById<RecyclerView>(R.id.search_nameday_suggestions)
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
