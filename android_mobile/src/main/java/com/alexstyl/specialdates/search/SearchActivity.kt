package com.alexstyl.specialdates.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
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
//        val namesSuggestionsView = findViewById<RecyclerView>(R.id.search_nameday_suggestions)

        val resultsAdapter = SearchResultsAdapter(imageLoader, labelCreator, object : SearchResultClickListener {
            override fun onContactClicked(contact: Contact) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNamedayClicked(date: Date) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        resultView.adapter = resultsAdapter
        searchResultView = AndroidSearchResultView(resultsAdapter, searchbar)
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
